package solution;

public class SnakesAndLadders {

	private static class Game {
		private Board board;
		private boolean[] visited;
        
		private static final int MAX_DICE = 6;
		
		public Game(Board board) {
			this.board = board;
            this.visited = new boolean[board.size()];
            for (int i = 0; i < visited.length; i++)
                visited[i] = false;
		}
		
		public int solve() {
			return solve(0, 1, -1);
		}

		private int solve(int pos, int currentTurn, int minTurns) {
			if ((currentTurn >= minTurns && minTurns != -1) || visited[pos] == true)
				return minTurns;
			
			if (pos+MAX_DICE >= board.size()-1) 
				return pos == 99 ? currentTurn - 1 : currentTurn;
			
            visited[pos] = true;
            
			for (int i = pos+1; i <= pos+MAX_DICE; i++)
				if (board.isLadder(i))
					minTurns = solve(board.get(i), currentTurn + 1, minTurns);
			
            boolean spaceChecked = false;
			for (int i = pos+MAX_DICE; i >= pos+1; i--) {
                if (board.isSnake(i))
				    minTurns = solve(board.get(i), currentTurn + 1, minTurns);
                else if (!spaceChecked && board.isSpace(i)) {
                    spaceChecked = true;
                    minTurns = solve(i, currentTurn + 1, minTurns);
                }
            }
			
            visited[pos] = false;
                
			return minTurns;
		}
	}
	
	private static class Board {
		private static final int SIZE = 100;
		private int[] board;
		
		public Board() {
			board = new int[SIZE];
			for (int i = 0; i < board.length; i++)
				board[i] = i;
		}
		
		public boolean isSpace(int i) {
			return board[i] == i;
		}
		
		public boolean isSnake(int i) {
			return board[i] < i;
		}
		
		public boolean isLadder(int i) {
			return board[i] > i;
		}
		
		public int get(int i) {
			return board[i];
		}
		
		public void set(int from, int to) {
			board[from] = to;
		}
		
		public int size() {
			return board.length;
		}
	}
	
	public static void main(String[] args) {
		Board board = new Board();
		board.set(36, 99);
		board.set(2, 53);
		board.set(55, 32);
		
		Game game = new Game(board);
		
		System.out.println(board.isLadder(36));
		
		System.out.println(game.solve());
	}

}
