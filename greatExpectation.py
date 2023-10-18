from airflow.models import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime
import great_expectations as gx
import requests
import pandas as pd
from datetime import datetime
import unidecode
import sys, os
sys.path.append(os.path.join(os.path.dirname(os.path.realpath(__file__)), '/home/talentum/Desktop/airflow-tutorial/GreatExpectations/great_expectations'))

from plugins import expect_column_values_to_match_cloud_word_in_description




def fetch_data_and_push(**kwargs):
    token = "ddd2233366783b2c533e6082d74b2e2443da1ddc"
    boundurl = "https://api.waqi.info/map/bounds/?latlng=7.36247,67.67578,35.88905,97.38281&token=" + token

    response = requests.get(boundurl)
    data = response.json()['data']

    df = pd.json_normalize(data)
    for i in df.index:
        if "India" not in df['station.name'][i]:
            df.drop(i, inplace=True)
    print(df)

    api_key = "4a7d2f914303f1681cbac2d3d9f27ce1"
    station_data_lst = []
    for i in df.index:
        stationUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + str(
            df['lat'][i]) + "&lon=" + str(df['lon'][i]) + "&units=metric&appid=" + api_key
        station_response = requests.get(stationUrl)
        station_data = station_response.json()
        sd_dict = {
            'cityname': station_data['name'],
            "datetime": datetime.fromtimestamp(station_data['dt']).strftime('%d-%m-%y'),
            "cloud": station_data['weather'][0]['main'] if 'main' in station_data['weather'][0] else 'None',
            "cloud description": station_data['weather'][0]['description'] if 'description' in station_data[
                'weather'][0] else 'None',
            "temp": station_data['main']['temp'] if 'temp' in station_data['main'] else 'None',
            "pressure": station_data['main']['pressure'] if 'pressure' in station_data['main'] else 'None',
            "humidity_percent": station_data['main']['humidity'] if 'humidity' in station_data['main'] else 'None',
            "visibility_m": station_data['visibility'] if 'visibility' in station_data else 'None',
            "windspeed_mps": station_data['wind']['speed'] if 'speed' in station_data['wind'] else 'None',
            "wind_direction_deg": station_data['wind']['deg'] if 'deg' in station_data['wind'] else 'None',
            "cloudiness_percent": station_data['clouds']['all'] if 'all' in station_data['clouds'] else 'None',
            "sunrise": datetime.fromtimestamp(station_data['sys']['sunrise']).strftime('%d-%m-%y %H:%M:%S'),
            'sunset': datetime.fromtimestamp(station_data['sys']['sunset']).strftime('%d-%m-%y %H:%M:%S')
        }
        station_data_lst.append(sd_dict)

    data_df = pd.json_normalize(station_data_lst)
    data_df['cityname'] = data_df['cityname'].apply(lambda x: unidecode.unidecode(x))
    data_df.convert_dtypes()    
    kwargs['ti'].xcom_push(key='data_df', value=data_df)

def apply_expectations_and_validate_data(**kwargs):  
    ti = kwargs['ti']
    data_df = ti.xcom_pull(key='data_df', task_ids='fetch_data_task')

    context = gx.get_context()
    validator = context.sources.pandas_default.read_dataframe(data_df)
    validator.expect_column_values_to_not_be_null("temp")
    validator.expect_column_values_to_be_between("temp", 1, 45)
    #validator.expect_column_values_to_match_cloud_word_in_description("cloud description")
    results = validator.validate()
    validation_success = all(result["success"] for result in results.results)
    ti.xcom_push(key='validation_success', value=validation_success)
    ti.xcom_push(key='data_df', value=data_df)


def check_validation_results(**kwargs):   
    ti = kwargs['ti']
    validation_success = ti.xcom_pull(key='validation_success', task_ids='apply_expectations_task') 
    data_df = ti.xcom_pull(key='data_df', task_ids='apply_expectations_task')
      
    if validation_success:
        print("Data validation successful. Saving data to hdfs.")
        #data_df.to_csv('hdfs://localhost:9000/user/talentum/wheaterdata.csv')
    else:
        print("Data validation failed. Alerting system.")  
       

dag = DAG(
    'data_validation_dag',
    start_date=datetime(2023, 8, 7),
    schedule_interval=None,
)

fetch_data_task = PythonOperator(
    task_id='fetch_data_task',
    python_callable=fetch_data_and_push,
    provide_context=True,
    dag=dag,
)

apply_expectations_task = PythonOperator(
    task_id='apply_expectations_task',
    python_callable=apply_expectations_and_validate_data,
    provide_context=True,
    dag=dag,
)

check_results_task = PythonOperator(
    task_id='check_results_task',
    python_callable=check_validation_results,
    provide_context=True,
    dag=dag,
)

fetch_data_task >> apply_expectations_task >> check_results_task