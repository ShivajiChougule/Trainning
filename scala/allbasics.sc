//Val (Values are immnutables)
val helllo : String = "Hello World"
helllo
//Var (variables are mutable)
var helloVar : String = "Hello Therer"
helloVar = helllo + " Good Moring"
println(helloVar)

/*
DATA TYPES
Boolean = true or false
Byte = -128 t0 127
Short = 8
Int = 10
Float = 10.4f
Double = 12.56
Long = 1626378588689669606
Char = 'a'
String  = 'Hollo World'
 */

val Numbers : Int = 1872468
val Numbderfloat : Float = 156.670f
val NumberDouble : Double = 616638.9078
val isItBool : Boolean = true
val NumberByte : Byte = -128
val NumberShort : Short = 6374
val NumberLong : Long = 737848992
val Charcter : Char = 'b'
val Strings : String = "Hello World"

println(f"Number is $Numbers " + "\r\n" +
  f"Float is $Numbderfloat%.5f " + "\r\n" +
  f"Boolean is $isItBool ")

println(s"I can use s prefix to use varibles like $isItBool and $Numbers")
println(s"Expression in String like $Numbers multiply by $NumberByte is ${Numbers*NumberByte}")

val ultimateAns : String = "To life, the universe, and everything is 42"
val pattern = """.* ([\d]+).*""".r
val pattern(ansrstring)=ultimateAns
val ans = ansrstring.toInt
println(ans)

val isGreater = 1 > 2
val isLesser  = 1 < 2
val logicalAnd = isGreater && isLesser
val logicalOr = isGreater || isLesser

// Comparing
val picard : String = "Picard"
val isCaptain : String = "Picard"
val isBestCap : Boolean = picard == isCaptain
println(isBestCap)