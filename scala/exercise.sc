/*
Write some code that take value of pi, double it and then prints within a string
with three decimal places of precision to the right
 */

val pi = 3.14159265
val StingPi = pi * 2
println(f" Three Decimal within string will be $StingPi%.3f ")

//IF ELSE
if (1>3 ){
  println("One is greater than Three")
}else{
  println("Three is greater than One")
}

//CASE
val number : Int = 24
number match {
  case 1 =>
    println("One")
  case 2 =>
    println("Two")
  case 3 =>
    println("Three")
  case _ =>
    println("Something else")
}

//For loop

for (x <- 1 to 4){
  val squared = x*x
  println(squared)
}

//While loop var
var x : Int = 10
while (x >= 0){
  println(x)
  x -=1
}
var x : Int = 10
do{println(x) ; x+=1} while (x<=10)

//Fibonacci series first 10 value
var a = 0
var b = 1
val num = 7
if (num < 0){
  println("num should be positive")
}else if (num == a){
  println(List(a))
}else if (num == b ){
  println(List(a,b))
}else {
  var febList = List(a,b)
  for (_ <- 2 to num) {
    val next = a + b
    febList = febList :+ next
    a = b
    b = next
  }
  println(febList)
}

// DATA STRUCTURE

// Tuples
val anyTuple = ("Shiva",27,"Trainee","Pune",2023)
println(anyTuple._1)
println(anyTuple._4)

// Lists
val anyList = List("name","another","onemore", "again")
println(anyList(0))
println(anyList.head)
println(anyList.take(3))

for(elements <- anyList) {println(elements)}

val backWordAnyList = anyList.map((element : String) => {element.reverse})

for (elements <- backWordAnyList) {println(elements)}

val anyListNums = List(1,2,3,4,5)

val sum = anyListNums.reduce((x : Int, y : Int) => x+ y )
println(sum)

//or

val sum = anyListNums.sum
println(sum)

val odds = anyListNums.filter((x : Int) => (x%2) != 0)
println(odds)

val listConct = List(1,3,5,7) ++ List(2,4,6)
println(listConct)

//OR

val listConct = List(1,3,5,7) ::: List(2,4,6)
println(listConct)

val sorted = listConct.sorted
println(sorted)

val revereSorted = listConct.sorted(Ordering.Int.reverse)
println(revereSorted)

val maxim = listConct.max
println(maxim)

val hasNum = listConct.contains(3)
println(hasNum)

//MAPS
val anyMap = Map(("Name","Shivaji"),("Age",26),("Position","Trainee"),("Location","Pune"))
println(anyMap("Name"))
println(anyMap.contains("Location"))

val doesContains = util.Try(anyMap("Age")) getOrElse "Not Found"
println(doesContains)

val doesContains = util.Try(anyMap("Year")) getOrElse "Not Found"
println(doesContains)


//Exercise
val oneToTwenty = List.range(1,21)
println(oneToTwenty)
val divisibleByThree = oneToTwenty.filter((y : Int) => (y%3) == 0)
println(divisibleByThree)
