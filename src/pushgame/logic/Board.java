package pushgame.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pushgame.util.GlobalSettings;
import pushgame.util.HashHelper;

public class Board implements Serializable {

	private static final long serialVersionUID = 6328337622665137540L;

	/**
	 * Stores player1's pawns positions.
	 */
	private long player1Board;
	/**
	 * Stores player2's pawns positions.
	 */
	private long player2Board;
	/**
	 * Board's hash.
	 */
	private long hash;
	/**
	 * Player1's id.
	 */
	private static byte player1 = GlobalSettings.PLAYER_1_ID;
	/**
	 * Player2's id.
	 */
	private static byte player2 = GlobalSettings.PLAYER_2_ID;
	/**
	 * Player1's initial pawns positions.
	 */
	private static long player1Initial = GlobalSettings.PLAYER_1_INITIAL;
	/**
	 * Player2's initial pawns positions.
	 */
	private static long player2Initial = GlobalSettings.PLAYER_2_INITIAL;
	/**
	 * Board size (width).
	 */
	private static byte size = GlobalSettings.BOARD_SIZE;
	/**
	 * Bit masks for checking & setting pawn's position on board.
	 */
	private static long[] fieldsMasks = Arrays.copyOf(
			GlobalSettings.BOARD_FIELDS_MASKS,
			GlobalSettings.BOARD_FIELDS_MASKS.length);
	/**
	 * Bit masks for deleting pawn from given field.
	 */
	private static long[] fieldsMasksZero = Arrays.copyOf(
			GlobalSettings.BOARD_FIELDS_MASKS_ZERO,
			GlobalSettings.BOARD_FIELDS_MASKS_ZERO.length);
	/**
	 * Field id's. Speeds up field searching.<br>
	 * <i>Example: [row][column] => row * size + column.</i>
	 */
	private static byte[][] fieldsNums = Arrays.copyOf(
			GlobalSettings.BOARD_FIELDS_NUMS,
			GlobalSettings.BOARD_FIELDS_NUMS.length);

	/**
	 * Default constructor. Creates empty board (without pawns on it).
	 */
	public Board() {
		player1Board = 0;
		player2Board = 0;
		hash = 0;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param board
	 *            Board object to copy.
	 */
	public Board(Board board) {
		this.player1Board = board.player1Board;
		this.player2Board = board.player2Board;
		this.hash = board.hash;
	}

	/**
	 * Gets value of given field. Returns:<br>
	 * <ul>
	 * <li><b>0</b> => if field is empty;</li>
	 * <li><b>player's id</b> => if field is not empty.</li>
	 * </ul>
	 * 
	 * @param row
	 *            Row of the board. 0 on top.
	 * @param column
	 *            Column of the board. 0 on left.
	 * @return Value of the field.
	 */
	public byte get(byte row, byte column) {
		byte value = 0;
		if ((player1Board & fieldsMasks[fieldsNums[row][column]]) != 0)
			value = player1;
		else if ((player2Board & fieldsMasks[fieldsNums[row][column]]) != 0)
			value = player2;
		return value;
	}

	/**
	 * Gets value of given field. Returns:<br>
	 * <ul>
	 * <li><b>0</b> => if field is empty;</li>
	 * <li><b>player's id</b> => if field is not empty.</li>
	 * </ul>
	 * 
	 * @param field
	 *            Field's id. 0 on top-left corner, 63 on bottom-right corner.
	 * @return Value of the field.
	 */
	public byte get(byte field) {
		byte value = 0;
		if ((player1Board & fieldsMasks[field]) != 0)
			value = player1;
		else if ((player2Board & fieldsMasks[field]) != 0)
			value = player2;
		return value;
	}

	/**
	 * Sets value of given field.
	 * 
	 * @param row
	 *            Row of the field.
	 * @param column
	 *            Column of the field.
	 * @param value
	 *            Value to set: <br>
	 *            <ul>
	 *            <li><b>0</b> => clears field;</li>
	 *            <li><b>player's id</b> => sets player's pawn.</li>
	 *            </ul>
	 */
	public void set(byte row, byte column, byte value) {
		if (value == 0) {
			player1Board = player1Board
					& fieldsMasksZero[fieldsNums[row][column]];
			player2Board = player2Board
					& fieldsMasksZero[fieldsNums[row][column]];
		}
		if (value == player1)
			player1Board = player1Board | fieldsMasks[fieldsNums[row][column]];
		else if (value == player2)
			player2Board = player2Board | fieldsMasks[fieldsNums[row][column]];
	}

	/**
	 * Sets value of given field.
	 * 
	 * @param field
	 *            Field's id. 0 on top-left corner, 63 on bottom-right corner.
	 * @param value
	 *            Value to set: <br>
	 *            <ul>
	 *            <li><b>0</b> => clears field;</li>
	 *            <li><b>player's id</b> => sets player's pawn.</li>
	 *            </ul>
	 */
	public void set(byte field, byte value) {
		if (value == 0) {
			player1Board = player1Board & fieldsMasksZero[field];
			player2Board = player2Board & fieldsMasksZero[field];
		} else if (value == player1)
			player1Board = player1Board | fieldsMasks[field];
		else if (value == player2)
			player2Board = player2Board | fieldsMasks[field];
	}

	/**
	 * Checks if game is over.<br>
	 * 
	 * @return <ul>
	 *         <li><b>0</b> => game still in progress;</li>
	 *         <li><b>player's id</b> => player wins.</li>
	 *         </ul>
	 */
	public byte getWinner() {
		if (player1Board == player2Initial)
			return player1;
		if (player2Board == player1Initial)
			return player2;
		return (byte) 0;
	}

	/**
	 * Initializes board with pawns.
	 */
	public void initialize() {
		this.player1Board = player1Initial;
		this.player2Board = player2Initial;
	}

	/**
	 * Returns size of board (width).
	 * 
	 * @return Size (width) of board.
	 */
	public byte getSize() {
		return size;
	}

	/**
	 * Returns number of pushed pawns with move from given field with given
	 * angle.
	 * 
	 * @param field
	 *            Beginning of the move.
	 * @param angle
	 *            Angle of the move.
	 * @return Number of pushed pawns.
	 */
	protected byte getChainLength(byte field, byte angle) {
		byte chainLength = 0;
		byte value = get(field);
		byte curr = field;
		byte prev = field;
		// 7 | 0 | 1
		// 6 | X | 2
		// 5 | 4 | 3
		switch (angle) {
		/* ******** angle == 0 ******** */
		case 0:
			curr = (byte) (field - 8);
			while (curr >= 0) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				curr = (byte) (curr - 8);
			}
			if (curr < 0)
				chainLength = 0;
			break;

		/* ******** angle == 1 ******** */
		case 1:
			curr = (byte) (prev - 7);
			while (curr >= 0 && (curr / 8) == ((prev / 8) - 1)) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				prev = curr;
				curr = (byte) (prev - 7);
			}
			if (curr < 0 || (curr / 8) != ((prev / 8) - 1))
				chainLength = 0;
			break;

		/* ******** angle == 2 ******** */
		case 2:
			curr = (byte) (field + 1);
			while (curr < 64 && (curr / 8) == (field / 8)) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				curr = (byte) (curr + 1);
			}
			if (curr > 63 || (curr / 8) != (field / 8))
				chainLength = 0;
			break;

		/* ******** angle == 3 ******** */
		case 3:
			curr = (byte) (prev + 9);
			while (curr < 64 && (curr / 8) == ((prev / 8) + 1)) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				prev = curr;
				curr = (byte) (prev + 9);
			}
			if (curr > 63 || (curr / 8) != ((prev / 8) + 1))
				chainLength = 0;
			break;

		/* ******** angle == 4 ******** */
		case 4:
			curr = (byte) (field + 8);
			while (curr < 64) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				curr = (byte) (curr + 8);
			}
			if (curr > 63)
				chainLength = 0;
			break;

		/* ******** angle == 5 ******** */
		case 5:
			curr = (byte) (prev + 7);
			while (curr < 64 && (curr / 8) == ((prev / 8) + 1)) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				prev = curr;
				curr = (byte) (prev + 7);
			}
			if (curr > 63 || (curr / 8) != ((prev / 8) + 1))
				chainLength = 0;
			break;

		/* ******** angle == 6 ******** */
		case 6:
			curr = (byte) (field - 1);
			while (curr >= 0 && (curr / 8) == (field / 8)) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				curr = (byte) (curr - 1);
			}
			if (curr < 0 || (curr / 8) != (field / 8))
				chainLength = 0;
			break;

		/* ******** angle == 7 ******** */
		case 7:
			curr = (byte) (prev - 9);
			while (curr >= 0 && (curr / 8) == ((prev / 8) - 1)) {
				if (get(curr) == value) {
					++chainLength;
				} else {
					if (get(curr) != 0) {
						chainLength = 0;
						break;
					} else
						break;
				}
				prev = curr;
				curr = (byte) (prev - 9);
			}
			if (curr < 0 || (curr / 8) != ((prev / 8) - 1))
				chainLength = 0;
			break;

		}
		return chainLength;
	}

	/**
	 * Returns number of empty fields "behind" given field with given angle.<br>
	 * <b>Return value is always <0, 3></b>
	 * 
	 * @param field
	 *            Field from we are counting empty fields.
	 * @param angle
	 *            Angle of empty fields searching.
	 * @return Number of empty fields
	 */
	protected byte countNextEmptyFields(byte field, byte angle) {
		byte count = 0;
		byte curr = field;
		byte prev = field;

		switch (angle) {

		case 0:
			curr = (byte) (field - 8);
			while (curr >= 0 && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				curr = (byte) (curr - 8);
			}
			break;

		case 1:
			curr = (byte) (prev - 7);
			while (curr >= 0 && (curr / 8) == ((prev / 8) - 1) && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				prev = curr;
				curr = (byte) (prev - 7);
			}
			break;

		case 2:
			curr = (byte) (field + 1);
			while (curr < 64 && (curr / 8) == (field / 8) && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				curr = (byte) (curr + 1);
			}
			break;

		case 3:
			curr = (byte) (prev + 9);
			while (curr < 64 && (curr / 8) == ((prev / 8) + 1) && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				prev = curr;
				curr = (byte) (prev + 9);
			}
			break;

		case 4:
			curr = (byte) (field + 8);
			while (curr < 64 && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				curr = (byte) (curr + 8);
			}
			break;

		case 5:
			curr = (byte) (prev + 7);
			while (curr < 64 && (curr / 8) == ((prev / 8) + 1) && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				prev = curr;
				curr = (byte) (prev + 7);
			}
			break;

		case 6:
			curr = (byte) (field - 1);
			while (curr >= 0 && (curr / 8) == (field / 8) && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				curr = (byte) (curr - 1);
			}
			break;

		case 7:
			curr = (byte) (prev - 9);
			while (curr >= 0 && (curr / 8) == ((prev / 8) - 1) && count < 3) {
				if (get(curr) == 0)
					++count;
				else
					break;
				prev = curr;
				curr = (byte) (prev - 9);
			}
			break;

		}

		return count;
	}

	/**
	 * Returns list of all possible moves (Movement objects) for given player.
	 * 
	 * @param player
	 *            Player's id.
	 * @return List of all possible moves.
	 */
	public List<Movement> getPossibleMovesAll(byte player) {
		List<Movement> moves = new ArrayList<Movement>();
		byte temp = 0;
		byte chain = 0;
		byte empty = 0;
		long playerBoard = 0;
		long enemyBoard = 0;
		if (player == player1) {
			playerBoard = player1Board;
			enemyBoard = player2Board;
		} else {
			playerBoard = player2Board;
			enemyBoard = player1Board;
		}

		for (byte i = 0; i < 64; ++i) {
			if ((playerBoard & fieldsMasks[i]) != 0) {

				/* ******************* angle == 0 ******************* */
				temp = (byte) ((byte) i - 8);
				if (temp >= 0) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 0);
						if (chain != 0) {
							empty = countNextEmptyFields(
									(byte) (i - (chain * 8)), (byte) 0);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 0, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 0);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 0, (byte) 0));
						}
					}
				}
				/* ************************************************** */

				/* ******************* angle == 1 ******************* */
				temp = (byte) ((byte) i - 7);
				if (temp >= 0) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 1);
						if (chain != 0) {
							empty = countNextEmptyFields(
									(byte) (i - (chain * 7)), (byte) 1);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 1, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 1);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 1, (byte) 0));
						}
					}
				}
				/* ************************************************** */

				/* ******************* angle == 2 ******************* */

				temp = (byte) ((byte) i + 1);
				if (temp < 64 && (temp / 8) == (temp / 8)) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 2);
						if (chain != 0) {
							empty = countNextEmptyFields((byte) (i + chain),
									(byte) 2);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 2, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 2);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 2, (byte) 0));
						}
					}
				}

				/* ************************************************** */

				/* ******************* angle == 3 ******************* */

				temp = (byte) ((byte) i + 9);
				if (temp < 64) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 3);
						if (chain != 0) {
							empty = countNextEmptyFields(
									(byte) (i + (9 * chain)), (byte) 3);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 3, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 3);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 3, (byte) 0));
						}
					}
				}

				/* ************************************************** */

				/* ******************* angle == 4 ******************* */

				temp = (byte) ((byte) i + 8);
				if (temp < 64) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 4);
						if (chain != 0) {
							empty = countNextEmptyFields(
									(byte) (i + (8 * chain)), (byte) 4);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 4, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 4);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 4, (byte) 0));
						}
					}
				}

				/* ************************************************** */

				/* ******************* angle == 5 ******************* */
				temp = (byte) ((byte) i + 7);
				if (temp < 64) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 5);
						if (chain != 0) {
							empty = countNextEmptyFields(
									(byte) (i + (7 * chain)), (byte) 5);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 5, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 5);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 5, (byte) 0));
						}
					}
				}

				/* ************************************************** */

				/* ******************* angle == 6 ******************* */

				temp = (byte) ((byte) i - 1);
				if (temp >= 0 && (temp / 8) == (temp / 8)) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 6);
						if (chain != 0) {
							empty = countNextEmptyFields((byte) (i - chain),
									(byte) 6);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 6, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 6);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 6, (byte) 0));
						}
					}
				}

				/* ************************************************** */

				/* ******************* angle == 7 ******************* */
				temp = (byte) ((byte) i - 9);
				if (temp >= 0) {
					if ((playerBoard & fieldsMasks[temp]) != 0) {
						chain = getChainLength(i, (byte) 7);
						if (chain != 0) {
							empty = countNextEmptyFields(
									(byte) (i - (chain * 9)), (byte) 7);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 7, chain));
							}
						}
					} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
						empty = countNextEmptyFields(i, (byte) 7);
						for (byte dist = 1; dist <= empty; ++dist) {
							moves.add(new Movement(i, dist, (byte) 7, (byte) 0));
						}
					}
				}
				/* ************************************************** */
			}
		}
		Collections.shuffle(moves);
		return moves;
	}

	/**
	 * Returns list of possible moves (Movement objects) for given player<br>
	 * with priorities for directions:<br>
	 * <i> forward -> inline -> backward</i><br>
	 * <b>Returns only moves in one, chosen (by priorities) direction!</b>
	 * 
	 * @param player
	 *            Player's id.
	 * @return List of all possible moves.
	 */
	public List<Movement> getPossibleMoves(byte player) {
		List<Movement> moves = new ArrayList<Movement>();
		byte temp = 0;
		byte chain = 0;
		byte empty = 0;
		long playerBoard = 0;
		long enemyBoard = 0;
		if (player == player1) {
			playerBoard = player1Board;
			enemyBoard = player2Board;
		} else {
			playerBoard = player2Board;
			enemyBoard = player1Board;
		}
		// 7 | 0 | 1
		// 6 | X | 2
		// 5 | 4 | 3
		boolean forwardMoves = true;
		int stage = 0;

		while (moves.isEmpty()) {
			if (stage == 1)
				forwardMoves = false;

			for (byte i = 0; i < 64; ++i) {
				if ((playerBoard & fieldsMasks[i]) != 0) {

					if ((forwardMoves && player == player1)
							|| (!forwardMoves && player == player2)) {
						/* ******************* angle == 3 ******************* */

						temp = (byte) ((byte) i + 9);
						if (temp < 64) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 3);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i + (9 * chain)), (byte) 3);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 3, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 3);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 3,
											(byte) 0));
								}
							}
						}

						/* ************************************************** */

						/* ******************* angle == 4 ******************* */

						temp = (byte) ((byte) i + 8);
						if (temp < 64) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 4);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i + (8 * chain)), (byte) 4);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 4, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 4);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 4,
											(byte) 0));
								}
							}

							/* ************************************************** */

							/*
							 * ******************* angle == 5
							 * *******************
							 */
							temp = (byte) ((byte) i + 7);
							if (temp < 64) {
								if ((playerBoard & fieldsMasks[temp]) != 0) {
									chain = getChainLength(i, (byte) 5);
									if (chain != 0) {
										empty = countNextEmptyFields(
												(byte) (i + (7 * chain)),
												(byte) 5);
										for (byte dist = 1; dist <= empty; ++dist) {
											moves.add(new Movement(i, dist,
													(byte) 5, chain));
										}
									}
								} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
									empty = countNextEmptyFields(i, (byte) 5);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 5, (byte) 0));
									}
								}
							}

							/* ************************************************** */
						}
					} else if ((forwardMoves && player == player2)
							|| (!forwardMoves && player == player1)) {
						/* ******************* angle == 0 ******************* */
						temp = (byte) ((byte) i - 8);
						if (temp >= 0) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 0);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i - (chain * 8)), (byte) 0);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 0, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 0);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 0,
											(byte) 0));
								}
							}
						}
						/* ************************************************** */

						/* ******************* angle == 1 ******************* */
						temp = (byte) ((byte) i - 7);
						if (temp >= 0) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 1);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i - (chain * 7)), (byte) 1);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 1, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 1);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 1,
											(byte) 0));
								}
							}
						}
						/* ************************************************** */

						/* ******************* angle == 7 ******************* */
						temp = (byte) ((byte) i - 9);
						if (temp >= 0) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 7);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i - (chain * 9)), (byte) 7);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 7, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 7);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 7,
											(byte) 0));
								}
							}
						}
						/* ************************************************** */
					}
					if (!forwardMoves) {
						/* ******************* angle == 2 ******************* */

						temp = (byte) ((byte) i + 1);
						if (temp < 64 && (temp / 8) == (temp / 8)) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 2);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i + chain), (byte) 2);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 2, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 2);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 2,
											(byte) 0));
								}
							}
						}

						/* ************************************************** */

						/* ******************* angle == 6 ******************* */

						temp = (byte) ((byte) i - 1);
						if (temp >= 0 && (temp / 8) == (temp / 8)) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 6);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i - chain), (byte) 6);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 6, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 6);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 6,
											(byte) 0));
								}
							}
						}

						/* ************************************************** */
					}

				}
			}
			++stage;
		}
		Collections.shuffle(moves);
		return moves;
	}

	/**
	 * Returns list of all possible moves (Movement objects) for given player's<br>
	 * <b>pawn without priorities<b>
	 * 
	 * @param player
	 *            Player's id.
	 * @param pawn
	 *            Selected pawn.
	 * @return List of all pawn's possible moves.
	 */
	public List<Movement> getPawnPossibleMoves(byte player, byte pawn) {
		List<Movement> moves = new ArrayList<Movement>();

		if (pawn < 0 || pawn > 63)
			return moves;

		byte temp = 0;
		byte chain = 0;
		byte empty = 0;
		long playerBoard = 0;
		long enemyBoard = 0;
		if (player == player1) {
			playerBoard = player1Board;
			enemyBoard = player2Board;
		} else {
			playerBoard = player2Board;
			enemyBoard = player1Board;
		}
		boolean forwardMoves = true;
		boolean backwardMoves = false;
		int stage = 0;

		while (moves.isEmpty() && stage < 3) {
			if (stage == 1) {
				forwardMoves = false;
				backwardMoves = false;
			} else if (stage == 2) {
				forwardMoves = false;
				backwardMoves = true;
			}

			byte i = pawn;
			if ((playerBoard & fieldsMasks[i]) != 0) {

				if ((forwardMoves && player == player1)
						|| (backwardMoves && player == player2)) {
					/* ******************* angle == 3 ******************* */

					temp = (byte) ((byte) i + 9);
					if (temp < 64) {
						if ((playerBoard & fieldsMasks[temp]) != 0) {
							chain = getChainLength(i, (byte) 3);
							if (chain != 0) {
								empty = countNextEmptyFields(
										(byte) (i + (9 * chain)), (byte) 3);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 3,
											chain));
								}
							}
						} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
							empty = countNextEmptyFields(i, (byte) 3);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 3,
										(byte) 0));
							}
						}
					}

					/* ************************************************** */

					/* ******************* angle == 4 ******************* */

					temp = (byte) ((byte) i + 8);
					if (temp < 64) {
						if ((playerBoard & fieldsMasks[temp]) != 0) {
							chain = getChainLength(i, (byte) 4);
							if (chain != 0) {
								empty = countNextEmptyFields(
										(byte) (i + (8 * chain)), (byte) 4);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 4,
											chain));
								}
							}
						} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
							empty = countNextEmptyFields(i, (byte) 4);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 4,
										(byte) 0));
							}
						}

						/* ************************************************** */

						/*
						 * ******************* angle == 5 *******************
						 */
						temp = (byte) ((byte) i + 7);
						if (temp < 64) {
							if ((playerBoard & fieldsMasks[temp]) != 0) {
								chain = getChainLength(i, (byte) 5);
								if (chain != 0) {
									empty = countNextEmptyFields(
											(byte) (i + (7 * chain)), (byte) 5);
									for (byte dist = 1; dist <= empty; ++dist) {
										moves.add(new Movement(i, dist,
												(byte) 5, chain));
									}
								}
							} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
								empty = countNextEmptyFields(i, (byte) 5);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 5,
											(byte) 0));
								}
							}
						}

						/* ************************************************** */
					}
				} else if ((forwardMoves && player == player2)
						|| (backwardMoves && player == player1)) {
					/* ******************* angle == 0 ******************* */
					temp = (byte) ((byte) i - 8);
					if (temp >= 0) {
						if ((playerBoard & fieldsMasks[temp]) != 0) {
							chain = getChainLength(i, (byte) 0);
							if (chain != 0) {
								empty = countNextEmptyFields(
										(byte) (i - (chain * 8)), (byte) 0);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 0,
											chain));
								}
							}
						} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
							empty = countNextEmptyFields(i, (byte) 0);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 0,
										(byte) 0));
							}
						}
					}
					/* ************************************************** */

					/* ******************* angle == 1 ******************* */
					temp = (byte) ((byte) i - 7);
					if (temp >= 0) {
						if ((playerBoard & fieldsMasks[temp]) != 0) {
							chain = getChainLength(i, (byte) 1);
							if (chain != 0) {
								empty = countNextEmptyFields(
										(byte) (i - (chain * 7)), (byte) 1);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 1,
											chain));
								}
							}
						} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
							empty = countNextEmptyFields(i, (byte) 1);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 1,
										(byte) 0));
							}
						}
					}
					/* ************************************************** */

					/* ******************* angle == 7 ******************* */
					temp = (byte) ((byte) i - 9);
					if (temp >= 0) {
						if ((playerBoard & fieldsMasks[temp]) != 0) {
							chain = getChainLength(i, (byte) 7);
							if (chain != 0) {
								empty = countNextEmptyFields(
										(byte) (i - (chain * 9)), (byte) 7);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 7,
											chain));
								}
							}
						} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
							empty = countNextEmptyFields(i, (byte) 7);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 7,
										(byte) 0));
							}
						}
					}
					/* ************************************************** */
				} else {
					/* ******************* angle == 2 ******************* */

					temp = (byte) ((byte) i + 1);
					if (temp < 64 && (temp / 8) == (temp / 8)) {
						if ((playerBoard & fieldsMasks[temp]) != 0) {
							chain = getChainLength(i, (byte) 2);
							if (chain != 0) {
								empty = countNextEmptyFields(
										(byte) (i + chain), (byte) 2);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 2,
											chain));
								}
							}
						} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
							empty = countNextEmptyFields(i, (byte) 2);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 2,
										(byte) 0));
							}
						}
					}

					/* ************************************************** */

					/* ******************* angle == 6 ******************* */

					temp = (byte) ((byte) i - 1);
					if (temp >= 0 && (temp / 8) == (temp / 8)) {
						if ((playerBoard & fieldsMasks[temp]) != 0) {
							chain = getChainLength(i, (byte) 6);
							if (chain != 0) {
								empty = countNextEmptyFields(
										(byte) (i - chain), (byte) 6);
								for (byte dist = 1; dist <= empty; ++dist) {
									moves.add(new Movement(i, dist, (byte) 6,
											chain));
								}
							}
						} else if ((enemyBoard & fieldsMasks[temp]) == 0) {
							empty = countNextEmptyFields(i, (byte) 6);
							for (byte dist = 1; dist <= empty; ++dist) {
								moves.add(new Movement(i, dist, (byte) 6,
										(byte) 0));
							}
						}
					}

					/* ************************************************** */
				}
			}
			++stage;
		}
		return moves;
	}

	/**
	 * Makes move on board.
	 * 
	 * @param move
	 *            Move to make.
	 */
	public void makeMove(Movement move) {
		byte originVal = get(move.getOrigin());

		if (move.chain == 0) {
			if (originVal == player1) {
				player1Board = player1Board & fieldsMasksZero[move.origin];
				player1Board = player1Board | fieldsMasks[move.destination];
				
				updateHashAfterMove(move.origin, move.destination, player1);
			} else {
				player2Board = player2Board & fieldsMasksZero[move.origin];
				player2Board = player2Board | fieldsMasks[move.destination];
				
				updateHashAfterMove(move.origin, move.destination, player2);
			}
			return;
		}

		long playerBoard = 0;
		if (originVal == player1)
			playerBoard = player1Board;
		else
			playerBoard = player2Board;

		byte tempFrom = 0;
		byte tempTo = 0;
		switch (move.getAngle()) {

		case 0:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin - (i * 8));
				tempTo = (byte) (move.destination - (i * 8));
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
			
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;

		case 1:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin - (i * 7));
				tempTo = (byte) (move.destination - (i * 7));
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
				
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;

		case 2:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin + i);
				tempTo = (byte) (move.destination + i);
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
				
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;

		case 3:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin + (i * 9));
				tempTo = (byte) (move.destination + (i * 9));
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
				
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;

		case 4:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin + (i * 8));
				tempTo = (byte) (move.destination + (i * 8));
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
				
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;

		case 5:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin + (i * 7));
				tempTo = (byte) (move.destination + (i * 7));
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
				
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;

		case 6:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin - i);
				tempTo = (byte) (move.destination - i);
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
				
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;

		case 7:
			for (byte i = move.chain; i > 0; --i) {
				tempFrom = (byte) (move.origin - (i * 9));
				tempTo = (byte) (move.destination - (i * 9));
				playerBoard = playerBoard & fieldsMasksZero[tempFrom];
				playerBoard = playerBoard | fieldsMasks[tempTo];
				
				updateHashAfterMove(tempFrom, tempTo, originVal);
			}
			break;
		}
		playerBoard = playerBoard & fieldsMasksZero[move.origin];
		playerBoard = playerBoard | fieldsMasks[move.destination];
		
		updateHashAfterMove(move.origin, move.destination, originVal);

		if (originVal == player1)
			player1Board = playerBoard;
		else
			player2Board = playerBoard;
	}

	/**
	 * Returns copy of current board after chosen move.
	 * 
	 * @param move
	 *            Move to make.
	 * @return Board copy after move.
	 */
	public Board getBoardCopyAfterMove(Movement move) {
		Board board = new Board(this);
		board.makeMove(move);
		return board;
	}

	/**
	 * Returns all possible moves that can resolve lock.<br>
	 * <i>Checks only 2 last (for given player) rows of board.<i>
	 * 
	 * @param player
	 *            Player who wants to resolve lock.
	 * @return Possible moves.
	 */
	public List<Movement> resolveLock(byte player) {
		List<Movement> moves = new ArrayList<Movement>();
		byte enemy = 0;
		byte step = 0;
		byte start = 0;
		byte stop = 0;

		if (player == player1) {
			enemy = player2;
			step = -1;
			start = 48;
			stop = 64;
		} else {
			enemy = player1;
			step = 1;
			start = 0;
			stop = 16;
		}

		for (byte i = start; i < stop; ++i) {
			if (get(i) == enemy) {
				moves.addAll(getPawnPossibleMoves(player,
						(byte) (i + (step * 8))));
			}
		}
		Collections.shuffle(moves);
		return moves;
	}

	public long getHash() {
		return this.hash;
	}
	
	public void updateHashAfterMove(byte from, byte to, byte player) {
		this.hash ^= HashHelper.ZOBRIST_KEYS[player-1][from];
		this.hash ^= HashHelper.ZOBRIST_KEYS[player-1][to];
	}
}
