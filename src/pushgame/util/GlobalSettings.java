package pushgame.util;

import java.awt.Color;
import java.math.BigInteger;

public class GlobalSettings {
	/*
	 * Players
	 */
	public static final byte PLAYER_1_ID = 1; // top
	public static final byte PLAYER_2_ID = 2; // bottom

	
	/*
	 * GUI
	 */
	public static final int GUI_BOARD_PANEL_SIZE = 512;
	public static final int GUI_BOARD_FIELD_SIZE = 64;
	public static final int GUI_BOARD_CHECKER_SIZE = 50;
	public static final Color GUI_FIELD_COLOR = new Color(207, 231, 245, 255);
	
	
	/*
	 * Board
	 */
	public static final byte BOARD_SIZE = 8;
	public static final long PLAYER_2_INITIAL = // -281474976710656  
			Long.parseLong("-281474976710656");
	public static final long PLAYER_1_INITIAL = //65535;
			Long.parseLong("65535");
	public static final long[] BOARD_FIELDS_MASKS = new long[] {
		Long.parseLong("1"), // [0][0]
		Long.parseLong("2"),
		Long.parseLong("4"),
		Long.parseLong("8"),
		Long.parseLong("16"),
		Long.parseLong("32"),
		Long.parseLong("64"),
		Long.parseLong("128"), // [0][7]
		
		Long.parseLong("256"), // [1][0]
		Long.parseLong("512"), 
		Long.parseLong("1024"), 
		Long.parseLong("2048"), 
		Long.parseLong("4096"), 
		Long.parseLong("8192"), 
		Long.parseLong("16384"), 
		Long.parseLong("32768"), // [1][7]
		
		Long.parseLong("65536"), // [2][0]
		Long.parseLong("131072"), 
		Long.parseLong("262144"), 
		Long.parseLong("524288"), 
		Long.parseLong("1048576"), 
		Long.parseLong("2097152"), 
		Long.parseLong("4194304"), 
		Long.parseLong("8388608"), // [2][7]
		
		Long.parseLong("16777216"), // [3][0] 
		Long.parseLong("33554432"),  
		Long.parseLong("67108864"), 
		Long.parseLong("134217728"), 
		Long.parseLong("268435456"), 
		Long.parseLong("536870912"), 
		Long.parseLong("1073741824"), 
		Long.parseLong("2147483648"), // [3][7]
		
		Long.parseLong("4294967296"), // [4][0]  
		Long.parseLong("8589934592"),   
		Long.parseLong("17179869184"),  
		Long.parseLong("34359738368"),
		Long.parseLong("68719476736"),
		Long.parseLong("137438953472"),
		Long.parseLong("274877906944"),
		Long.parseLong("549755813888"), // [4][7]
		
		Long.parseLong("1099511627776"), // [5][0] 
		Long.parseLong("2199023255552"),  
		Long.parseLong("4398046511104"), 
		Long.parseLong("8796093022208"), 
		Long.parseLong("17592186044416"), 
		Long.parseLong("35184372088832"), 
		new BigInteger("70368744177664").longValue(),
		new BigInteger("140737488355328").longValue(), // [5][7]
		
		new BigInteger("281474976710656").longValue(), // [6][0] 
		new BigInteger("562949953421312").longValue(), 
		new BigInteger("1125899906842624").longValue(),
		new BigInteger("2251799813685248").longValue(), 
		new BigInteger("4503599627370496").longValue(), 
		new BigInteger("9007199254740992").longValue(), 
		new BigInteger("18014398509481984").longValue(),
		new BigInteger("36028797018963968").longValue(), // [6][7] 

		new BigInteger("72057594037927936").longValue(), // [7][0] 
		new BigInteger("144115188075855872").longValue(),  
		new BigInteger("288230376151711744").longValue(),
		new BigInteger("576460752303423488").longValue(),
		new BigInteger("1152921504606846976").longValue(),  
		Long.parseLong("2305843009213693952"),  
		Long.parseLong("4611686018427387904"),  
		Long.parseLong("-9223372036854775808"),  // [7][7]
	};
	public static long[] BOARD_FIELDS_MASKS_ZERO = new long[] {
		Long.parseLong("-2"),
		Long.parseLong("-3"),
		Long.parseLong("-5"),
		Long.parseLong("-9"),
		Long.parseLong("-17"),
		Long.parseLong("-33"),
		Long.parseLong("-65"),
		Long.parseLong("-129"),
		
		Long.parseLong("-257"),
		Long.parseLong("-513"),
		Long.parseLong("-1025"),
		Long.parseLong("-2049"),
		Long.parseLong("-4097"),
		Long.parseLong("-8193"),
		Long.parseLong("-16385"),
		Long.parseLong("-32769"),

		Long.parseLong("-65537"),
		Long.parseLong("-131073"),
		Long.parseLong("-262145"),
		Long.parseLong("-524289"),
		Long.parseLong("-1048577"),
		Long.parseLong("-2097153"),
		Long.parseLong("-4194305"),
		Long.parseLong("-8388609"),
		
		Long.parseLong("-16777217"),
		Long.parseLong("-33554433"),
		Long.parseLong("-67108865"),
		Long.parseLong("-134217729"),
		Long.parseLong("-268435457"),
		Long.parseLong("-536870913"),
		Long.parseLong("-1073741825"),
		Long.parseLong("-2147483649"),
		
		Long.parseLong("-4294967297"),
		Long.parseLong("-8589934593"),
		Long.parseLong("-17179869185"),
		Long.parseLong("-34359738369"),
		Long.parseLong("-68719476737"),
		Long.parseLong("-137438953473"),
		Long.parseLong("-274877906945"),
		Long.parseLong("-549755813889"),
		
		Long.parseLong("-1099511627777"),
		Long.parseLong("-2199023255553"),
		Long.parseLong("-4398046511105"),
		Long.parseLong("-8796093022209"),
		Long.parseLong("-17592186044417"),
		Long.parseLong("-35184372088833"),
		Long.parseLong("-70368744177665"),
		Long.parseLong("-140737488355329"),
		
		Long.parseLong("-281474976710657"),
		Long.parseLong("-562949953421313"),
		Long.parseLong("-1125899906842625"),
		Long.parseLong("-2251799813685249"),
		Long.parseLong("-4503599627370497"),
		Long.parseLong("-9007199254740993"),
		Long.parseLong("-18014398509481985"),
		Long.parseLong("-36028797018963969"),
		
		Long.parseLong("-72057594037927937"),
		Long.parseLong("-144115188075855873"),
		Long.parseLong("-288230376151711745"),
		Long.parseLong("-576460752303423489"),
		Long.parseLong("-1152921504606846977"),
		Long.parseLong("-2305843009213693953"),
		Long.parseLong("-4611686018427387905"),
		Long.parseLong("9223372036854775807"),
		
	};
	public static byte[][] BOARD_FIELDS_NUMS = new byte[][] {
		{ 0,  1,  2,  3,  4,  5,  6,  7},
		{ 8,  9, 10, 11, 12, 13, 14, 15},
		{16, 17, 18, 19, 20, 21, 22, 23},
		{24, 25, 26, 27, 28, 29, 30, 31},
		{32, 33, 34, 35, 36, 37, 38, 39},
		{40, 41, 42, 43, 44, 45, 46, 47},
		{48, 49, 50, 51, 52, 53, 54, 55},
		{56, 57, 58, 59, 60, 61, 62, 63}
	};
}
