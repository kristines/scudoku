package scudoku.data

/**
 * Java2Scala port from judoku.data.OneToNine (adding a value to the enumeration for the "unknown" value)
 */

object UnknownOrOneToNine extends Enumeration {
	type UnknownOrOneToNine = Value  // needed to allow to use UnknownOrOneToNine as a synonym for UnknownOrOneToNine.Value
  
  val UNKNOWN = Value(".")		// TODO how to not have UNKNOWN here?! For potential values UNKNOWN is not a valid value (extending Enums does not work, same in Java!)
	val ONE   = Value("1")
	val TWO   = Value("2")
	val THREE = Value("3")
	val FOUR  = Value("4")
	val FIVE  = Value("5")
	val SIX   = Value("6")
	val SEVEN = Value("7")
	val EIGHT = Value("8")
	val NINE  = Value("9")	
	
	def fromString(s: String): UnknownOrOneToNine = {
	  s match {
	    case "1" => ONE
	    case "2" => TWO
	    case "3" => THREE
	    case "4" => FOUR
	    case "5" => FIVE
	    case "6" => SIX
	    case "7" => SEVEN
	    case "8" => EIGHT
	    case "9" => NINE
    	case "." => UNKNOWN
      case _   => throw new RuntimeException("Found value different than 1,2,..., 9 - wrong setup");
	  }
	}

	def fromChar(c: Char) = fromString(c.toString)
}