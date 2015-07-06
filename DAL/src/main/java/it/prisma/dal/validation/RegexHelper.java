package it.prisma.dal.validation;

/**
 * 
 * This class contains the list of the Regular Expressions used for the validation
 * 
 ************************************************************
 * Use this class paying attention: has not been tested yet *
 ************************************************************
 * 
 * @author Matteo Bassi
 *
 */
public class RegexHelper {

	/**
	 *  RULES:
	 *  	^				   				# Start of the line
	 *        ([A-Za-z0-9])					# must start with characters and number
	 *  	  +(?:[._-][A-Za-z0-9]+)*$      # Should contains characters and symbols in the list, a-z, A-Z, 0-9, ._-
	 *		$                 				# End of the line
	 */
	public static final String ALPHANUMERIC_DOT_UNDERSCORE_HYPHEN_PATTERN = "^([A-Za-z0-9])+(?:[._-][A-Za-z0-9]+)*$";
																			 
	
	/**
	 *  RULES:
	 *  	^                    # Start of the line
	 *  	  [A-Za-z0-9_-]	     # Must contains characters and symbols in the list, a-z, A-Z, 0-9, -, _
	 *      $                    # End of the line
	 */
	public static final String ALPHANUMERIC_PATTERN = "^([A-Za-z0-9_-]+)*$";
	
	
	public static final String ALPHANUMERIC_WHITESPACE_PATTERN = "^[a-zA-Z0-9][-a-zA-Z0-9 ]+$";
	
	/**
	 * 	RULES:
	 *		
	 *		^							# start of the line 
	 *		  [_A-Za-z0-9-]+			# must start with string in the bracket [ ], must contains one or more (+)
	 *		  (							# start of group #1
	 *		    \\.[_A-Za-z0-9-]+		# follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	 *		  )*						# end of group #1, this group is optional (*)
	 *		    @						# must contains a "@" symbol
	 *			  [A-Za-z0-9]+       	# follow by string in the bracket [ ], must contains one or more (+)
	 *			    (					# start of group #2 - first level TLD checking
	 *			      \\.[A-Za-z0-9]+  	# follow by a dot "." and string in the bracket [ ], must contains one or more (+)
	 *			    )*					# end of group #2, this group is optional (*)
	 *			    (					# start of group #3 - second level TLD checking
	 *		          \\.[A-Za-z]{2,}  	# follow by a dot "." and string in the bracket [ ], with minimum length of 2
	 *			    )					# end of group #3
	 *		$							# end of the line
	 * 
	 */
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

	
	/**
	 * 	RULES: 
	 * 
	 *		^							# start of the line 
	 * 		  (							# Start of group
	 *		    (?=.*\d)				# must contains one digit from 0-9
  	 *		    (?=.*[a-z])				# must contains one lowercase characters
     *		    (?=.*[A-Z])				# must contains one uppercase characters
     *          (?=.*[0-9])             # must contains one number
  	 *		    (?=.*[!@#$&*])			# must contains one special symbols in the list: ! @ # $ & *
	 *		  )							# End of group
	 *		$							# end of the line
	 * 
  	 */ 
	public static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,20}$";

	
	/**
	 * 	RULES: 
	 * 
	 * 		^		 					# start of the line
	 *		  #		 					# must constains a "#" symbols
  	 *		  (		 					# start of group #1
     *		    [A-Fa-f0-9]{6} 			# any strings in the list, with length of 6
  	 *		  	|						# ..or
  	 *	 		[A-Fa-f0-9]{3} 			# any strings in the list, with length of 3
	 *		  )							# end of group #1 
	 *		$							# end of the line
	 * 
	 */
	public static final String HEXADECIMAL_COLOR_CODE_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

}
