object HelloWorldObj {

  def InobjctMethod(): Unit = {
    println("InObjects method ")
  }

  def main(args:Array[String]): Unit = {
    val obj = new HelloWorld
    obj.display()
    InobjctMethod()
    obj.InobjctMethod()

  }

}
