

val increment = for (i <- 0 to 8; inc = i + 1) yield inc
increment
val log = "404 : Not found"

//Function
def error_msg(msg:String)={
  msg.split(":")(1).strip()
}
error_msg(log)
//error_msg.getClass()

//Function Object with another function
val status_error:String => String = error_msg
status_error.getClass

//Function Object with function literal
val  status_error = (msg:String)=> msg.split(":")(1).strip()
status_error(log)

//Higher Order Function

def convert(log_msg:String)=log_msg.toUpperCase //Normal function

def toUpper(msg:String,toUp:String=>String)={ // toUp is placeholder for function Convert
  if (msg != null) toUp(msg)
  else msg
}
//Method 1
toUpper(log,convert)
//Method 2
toUpper(log,(log_msg:String)=>log_msg.toUpperCase)
//Method 3
toUpper(log,_.toUpperCase)


//Genric fn style
def simpleOps[A,B](x:A,y:A,f:(A,A)=>B)=f(x,y)
simpleOps[Int,Boolean](2,4,_>_)
simpleOps[Int,Int](2,4,_+_)
simpleOps[String,String]("Scala "," Java",_+_)
simpleOps[String,Boolean]("Scala","s",_.contains(_))

def fact(x:Int,y:Int)=y%x==0
val isEven=fact(2,_)
isEven(4)
val DivBy_three=fact(3,_)
DivBy_three(10)

def printing_anything[A](x:A): Unit = println(x)

val myList = List(1,2,3,4,5,6,7,8,9,10)

myList.foreach(printing_anything)

val line =
  """=
    |This is 1st line.
    |This is 2nd line.
    |This is 3rd line.
    |""".stripMargin

val words = line.split("\n").flatMap(_.split("\\W+"))

words.foreach(printing_anything)
words.filter( _.length>2 ).sortWith(_.charAt(1)>_.charAt(1))// Ordring by 2nd index desc
words.filter(_.length>2).sortBy(_.charAt(1))(ord = Ordering.Char.reverse)
words.filter(_.length>2).sortBy(_.length)//asc
words.filter(_.length>2).sortBy(-_.length)//desc

myList.reduce((x,y)=>if (x>y)x else y) // max
myList.reduce((x,y)=>if (x<y)x else y) // min
myList.reduce((x,y)=>x+y) // sum
val (belowFive,aboveFive) =myList.partition(_<5) // creating partion
val belowFiveFirst = myList.partition(_<5)._1(1) // take will give List Instead of single value

val names = List(("Ram",39,"Pune"),("Sham",25,"Kolhapur"),("Kiran",20,"Sangli"))

val name = names.map {
  case (name,age,city)=>name
}
val namesBelowThirty= names.filter(_._2<30)
val namesBelowThirty = names.filter {
  case (_,age,_) if(age<30) =>true
  case _ =>false
}
val cityBelowAgeThirty = names.filter {
  case (_,age,_) if(age<30) =>true
  case _ =>false}.map(_._3)

val lookup:Map[String,String] = Map("S"->"Scala","J"->"Java","P"->"Python")

val name = lookup.get("S") // get return some(vale) or None
name.get // get method on some to unwrap
//some[value] or None
val name = lookup.get("R")
//name.get Error
val name = lookup.getOrElse("R","Unknown lang")

def isStringOrInt[B](y: B): String = {
  val typeIs = y match {
    case _: String => s"$y is String"
    case _: Int => s"$y is Int"
    case _ => "Unknown"
  }
  typeIs
}
isStringOrInt("python")
isStringOrInt(25)
isStringOrInt(23.567)

24.getClass.getSimpleName
"Python".getClass.getSimpleName

def getName(x:Any):String={
  val valueIs = lookup.get(x.toString) match {
    case None => "Unknown lang"
    case Some(value) => value
  }
  valueIs
}
getName("P")
getName("Z")
getName(None)
getName("S")
getName(25)

def getName(x:Any)={
  lookup.getOrElse(x.toString,"Unknown lang")
}
getName("P")
getName("Z")
getName(None)
getName("S")

class User(f:String,l:String){
  def this()={
    this("Shon","Tyson")
  }
  private val firstName = f
  var lastName = l
  override def toString: String = firstName
  def fullName:String= firstName  +" "+ lastName
  def getFirstName:String=firstName
}

val newUser = new User
val newUser2 = new User("Raj","Poul")
newUser.fullName
newUser2.lastName_=("Sham")
newUser2.fullName
newUser.getFirstName

class User(private val firstName:String, var lastName:String){
  def this()={
    this("Shon","Tyson")
  }
  override def toString: String = firstName
  def fullName:String= firstName  +" "+ lastName
  def getFirstName:String=firstName
}
val newUser = new User
val newUser2 = new User("Raj","Poul")
newUser.fullName
newUser2.lastName_=("Sharma")
newUser2.fullName
newUser.getFirstName

class User(val firstName:String="John", var lastName:String="Sena"){
  var age:Int = 10
  def this(f:String,l:String,a:Int)={
    this(f,l)
    age = a
  }
  override def toString: String = firstName
  def fullName:String= firstName  +" "+ lastName
  def getFirstName:String=firstName
}

val newUser = new User
val newUser2 = new User("Raj","Poul",30)
newUser.fullName
newUser.age
newUser2.lastName_=("Sharma")
newUser2.fullName
newUser2.age






// Function That returns function. Here ano lambda fn is return
def greeting(language: String) = (name: String) => {
  language match {
    case "english" => "Hello, " + name
    case "spanish" => "Buenos dias, " + name
  }
}

val spanishGreet = greeting("spanish")
spanishGreet("Shon")
val englishGreet = greeting("english")
englishGreet("Shon")

class ABC {
  override def toString: String = "Product Class"
  def product(x: Int, y: Int): Int = x * y //method
  val myProduct=(x:Int,y:Int )=>x*y //fn object with literal
  val myprouct:(Int,Int)=>Int=product // fn object from method
}
 val abcObj = new ABC
abcObj.product(20,5)
abcObj.myProduct(20,5)
abcObj.myprouct(20,5)

class User(val firstName:String="John", var lastName:String="Sena"){

  private var age:Int = 10
  private var salary:Int= 0
  def this(f:String,l:String,a:Int,s:Int)={
    this(f,l)
    age = a
    salary=s
  }
  override def toString: String = firstName
  def fullName:String= firstName  +" "+ lastName
  def getFirstName:String=firstName
  def getSalary:Int=User.totalSal(salary)
  def getLocation:String=User.location
}
object User{
  private val location = "Kolhapur"
  private def totalSal(sal:Int):Int=sal+1000
}
val newUser = new User
newUser.getSalary
newUser.getLocation
newUser.fullName
newUser.getFirstName
val newUser2 =new User("Shon","Parker",45,50000)
newUser2.getSalary
newUser.getLocation


//Factory D pattern
class Multiplier(val x:Int){
  def apply(y:Int):Int=x*y
}
object Multiplier{
  def apply(x:Int)=new Multiplier(x)
}
val double = Multiplier(2)
double.apply(5)
val triple =Multiplier(3)
triple(5)

//case class
class User1(val firstName:String,var lastName:String)
val newUser = new User1("Ram","Sharma")
newUser.firstName
newUser.lastName_=("Don")
newUser.lastName

// Need not to use new keyword coz companion Object created already. Automatically set methods are
//toString, getter, apply, copy, equals , hashcode and unapply
case class Character(name:String, isVillain:Boolean)
val batman = Character("Batman",false)
val joker = Character("Joker", true)
val superman = Character("Superman",false)

val jokerUnapply = Character unapply joker
println(
  jokerUnapply match {
    case None => "Nothing"
    case Some(value) => s"name is ${value._1} and is he villain? ${value._2}"
  }
)
//Internally unapply method will called, Object is extracted
//to match
val result = batman match{
  case Character("Joker", x) => s"is joker villain $x"
  case Character("Batman", y) => s"is batman villain $y"
}
result
val characters = List(batman,joker,superman)

val char_names = characters.map {
  case Character(n, _) => n
}
char_names
//Or
val char_names = characters.map(_.name)
char_names

case class Person(name:String,age:Int) {
  assert(name!=null && age!=null)}

def getPerson(name:String,age:Int):Option[Person]= for{
  firstname <- Option(name)
  ageOfPerson <- Option(age)
}yield Person(firstname,ageOfPerson)

val ram = getPerson("ram",56)

def count_vowels(str1:String): Int = {

  val vowels = List("a","e","i","o","u")
  var cnt = 0
  for(i <- str1.split("")){
    if (vowels.contains(i.toLowerCase)){
      cnt = cnt + 1
    }
  }
  cnt
}

println(count_vowels("hEllo"))

val vowels = List("a","e","i","o","u")
def count_vowels(s:String):Int=s.split("").map(_.toLowerCase).count(vowels.contains(_))
println(count_vowels("hEllo"))

def isAnagram[A,B](x:A,y:A,f:(A,A)=>B)=f(x,y)
isAnagram[String,Boolean]("liisten","silent",(x,y)=>x.sorted==y.sorted)

val numlist = List(1, 2, 3, 4, 5)
val target: Int = 6
def findPairs(x:List[Int],y:Int):List[(Int,Int)]={
  var foundPirs:List[(Int,Int)]=List()
  for (i <- x){
    for (j <- x){
      if (i+j == y){
        foundPirs = (i,j) :: foundPirs
      }
    }
  }
  val (halfList,_) = foundPirs.splitAt(foundPirs.length/2)
  halfList
}
println(findPairs(numlist,target))


def findPairs(x:List[Int],y:Int):List[(Int,Int)]={
  val pairs = x.flatMap(i=>x.map{j=>(i,j)}.filter{case(a,b)=>(a+b)==y})
  val halfList = pairs.take(pairs.length/2)
  halfList
}
println(findPairs(numlist,target))

def findPairsUsingfor(x:List[Int],y:Int):List[(Int,Int)]={
  val fullList = for{
    i <- x
    j <- x
    if i+j==y
  }yield (i,j)
  val halfList = fullList.take(fullList.length/2)
  halfList
}
def findPairsUsingFor(x: List[Int], y: Int): List[(Int, Int)] = {
  val fullList = for {
    (i, indexI) <- x.zipWithIndex
    (j, indexJ) <- x.zipWithIndex
    if i + j == y && indexI < indexJ
  } yield (i, j)
  fullList
}
println(findPairsUsingFor(numlist,target))

val colors = Array("green","red","blue","pink")
def sortingArray[A,B](inputData:Array[A],fun:Array[A]=>Array[B])=fun(inputData)

sortingArray[String,String](colors,_.sortBy(_.charAt(1)))


class NewMultiplier(x:Int){
  def apply(y:Int)=x*y
}
object NewMultiplier{
  def apply(x:Int)=new NewMultiplier(x)
}

val doublerfn = new NewMultiplier(2)
doublerfn(3)
 val triplerfn = new NewMultiplier(3)
 triplerfn(3)


//def byNameEx(x: => Int)={
//  //println(s"$x expr is printed") #as soon as x used, 2+3+56+1222 is calculated
//  x + 5
//}
//byNameEx(2+3+56+1222)
//
//def byValue(x: Long)={
//  println(x)
//  println(x)
//}
//
//def byName(x: =>Long)={
//  println(x)
//  println(x)
//}
//
//byValue(System.nanoTime())
//byName(System.nanoTime())
//
//def sync(x:Int):Int={
//  Thread.sleep(10000)
//  x+45
//}
//
//println(sync(5))
//println("sync Blocking")
//
//import scala.concurrent.Future
//import scala.concurrent.ExecutionContext.Implicits.global
//
//def async(x:Int):Future[Int]= Future{
//  Thread.sleep(10000)
//  x + 45
//}
//println(async(5))
//println("async Blocking")
//
//
////Await
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//def async(x: Int): Future[Int] = Future {
//  Thread.sleep(10000)
//  x + 45
//}
//
//val futureResult = async(5)
//val result = Await.result(futureResult, 15.seconds) // Wait for the future to complete within a given duration
//
//println(result)
//println("async Blocking with wait")
val nullRef:String = null
//nullRef.length runtime error

val nullType:Null = null
val nulRef:String=nullType
//nullType.length compile show error

val nilList:List[Int]= Nil
nilList.length // nil is valid object which has fields

val noneType:Option[Int]=None // sub-type of option
noneType.isEmpty
val nonNoneType2:Option[Int]=Some(45)
nonNoneType2.isEmpty

