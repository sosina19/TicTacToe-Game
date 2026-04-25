import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe extends JFrame implements ActionListener {
    private String inputX;
    private String inputO;
    private String playerX = inputX ;
    private String playerO = inputO;
     private boolean xTurn = true;
     private boolean vsAI = false;

    private JButton[][] buttons = new JButton[3][3];
   
    private JLabel status = new JLabel("");
    private JLabel xLabel = new JLabel("X: 0");
    private JLabel oLabel = new JLabel("O: 0");
    private JLabel drawLabel = new JLabel("Draw: 0");
    private JButton newgameBtn = new JButton("New Game");
    private JButton restartBtn = new JButton("Restart");

    private int xScore = 0;
    private int oScore = 0;
    private int drawScore = 0;

    public TicTacToe() {
      String[] modes = {"2 Players", "Play vs computer"};

  int choice = JOptionPane.showOptionDialog(
        this,
        "Choose Game Mode",
        "Tic Tac Toe",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        modes,
        modes[0]
  );

  vsAI = (choice == 1);

   if(!vsAI){
         inputX = JOptionPane.showInputDialog(this, "Enter Player X name:");
         inputO = JOptionPane.showInputDialog(this, "Enter Player O name:");
         
       if (inputX != null && !inputX.trim().isEmpty()) {
            playerX = inputX.trim();
        } else {
            playerX = "Player X";
        }

        if (inputO != null && !inputO.trim().isEmpty()) {
            playerO = inputO.trim();
        } else {
            playerO = "Player O";
        }
      
        }else{
            
     String input = JOptionPane.showInputDialog(this, "Enter your name:");

     String player = (input != null && !input.trim().isEmpty()) ? input : "Player";

     String[] sides = {"Play as X", "Play as O"};

         int side = JOptionPane.showOptionDialog(
                 this,
                "Choose your side",
                "Side Selection",
                JOptionPane.DEFAULT_OPTION,
                 JOptionPane.QUESTION_MESSAGE,
                 null,
                sides,
                sides[0]
                );

            if (side == 0) {
                playerX = player;
                playerO = "Computer";
                xTurn = true;
            } else {
                playerO = player;
                playerX = "Computer";
                xTurn = true;
            }
        }
         updateScore();
    status.setText((xTurn ? playerX : playerO) + " Turn");
    setVisible(true);

    if (vsAI && playerX.equals("Computer")) {
        aiMove();
    }

//the main window setup
        setTitle("XO Game");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // ===== MENU BAR =====
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(0, 45));
        menuBar.setBackground(new Color(30, 30, 30));
        menuBar.setOpaque(true);

        JMenu gameMenu = new JMenu("Game");
        gameMenu.setForeground(Color.WHITE);

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem exitItem = new JMenuItem("Exit");

        newGameItem.setBackground(new Color(45, 45, 45));
        newGameItem.setForeground(Color.WHITE);
        newGameItem.setOpaque(true);
        newgameBtn.addActionListener(e -> newGame());

        exitItem.setBackground(new Color(45, 45, 45));
        exitItem.setForeground(Color.WHITE);
        exitItem.setOpaque(true);

        newGameItem.addActionListener(e -> newGame());
        exitItem.addActionListener(e -> System.exit(0));

        gameMenu.add(newGameItem);
        gameMenu.add(exitItem);

        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 1));
        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setFont(new Font("Arial", Font.BOLD, 20)); 

        topPanel.add(status);
        add(topPanel, BorderLayout.NORTH);

        // Grid panel
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        gridPanel.setBackground(new Color(35, 35, 35));
        Font font = new Font("Arial", Font.BOLD, 50);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 50));
                buttons[i][j].setForeground(Color.white);
                buttons[i][j].setBackground(Color.DARK_GRAY);
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setOpaque(true);
             buttons[i][j].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
            ));
                buttons[i][j].addActionListener(this);
                gridPanel.add(buttons[i][j]);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 4));
        bottomPanel.setBackground(new Color(30, 30, 30));

        xLabel.setHorizontalAlignment(SwingConstants.CENTER);
        oLabel.setHorizontalAlignment(SwingConstants.CENTER);
        drawLabel.setHorizontalAlignment(SwingConstants.CENTER);

        xLabel.setForeground(Color.WHITE);
        oLabel.setForeground(Color.WHITE);
        drawLabel.setForeground(Color.WHITE);

        xLabel.setFont(new Font("Arial", Font.BOLD, 18));
        oLabel.setFont(new Font("Arial", Font.BOLD, 18));
        drawLabel.setFont(new Font("Arial", Font.BOLD, 18));

        restartBtn.setFont(new Font("Arial", Font.BOLD, 16));
        restartBtn.setBackground(new Color(53, 94, 59)); // calm green
        restartBtn.setForeground(Color.WHITE);
        restartBtn.setFocusPainted(false);
        restartBtn.addActionListener(e -> resetBoard());

        bottomPanel.add(xLabel);
        bottomPanel.add(oLabel);        // left
        bottomPanel.add(restartBtn);    // center
        bottomPanel.add(drawLabel);   // right

        add(bottomPanel, BorderLayout.SOUTH);  
         setVisible(true);   
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();

        if (!clicked.getText().equals("")) return;

        clicked.setText(xTurn ? "X" : "O");

        if (checkWin()) {
            if (xTurn) xScore++;
            else oScore++;

            updateScore();
            String winner = xTurn ? playerX : playerO;

           status.setText(winner + " Wins!");
             JOptionPane.showMessageDialog(this, winner + " Wins!");

    disableButtons();

            return;
        }

        if (checkDraw()) {
            drawScore++;
            updateScore();
            status.setText("Draw!");
      JOptionPane.showMessageDialog(this, "It's a Draw!");
        disableButtons();
            return;
        }

        xTurn = !xTurn;
        // Update status
       if (vsAI) {
        if (xTurn && playerX.equals("Computer")) {
            status.setText("Computer thinking...");
        } else if (!xTurn && playerO.equals("Computer")) {
            status.setText("Computer thinking...");
        } else {
            status.setText((xTurn ? playerX : playerO) + " Turn");
        }
            } else {
                status.setText((xTurn ? playerX : playerO) + " Turn");
            }

// AI Move
    if (vsAI) {

    boolean isComputerTurn =
            (playerX.equals("Computer") && xTurn) ||
            (playerO.equals("Computer") && !xTurn);

    if (isComputerTurn) {

        SwingUtilities.invokeLater(() -> {
            aiMove();
        });
    }
}
}

  private boolean checkWin() {
    String p = xTurn ? "X" : "O";

    // Rows
    for (int i = 0; i < 3; i++) {
        if (buttons[i][0].getText().equals(p) &&
            buttons[i][1].getText().equals(p) &&
            buttons[i][2].getText().equals(p)) {

            highlightWin(buttons[i][0], buttons[i][1], buttons[i][2]);
            return true;
        }
    }

    // Columns
    for (int i = 0; i < 3; i++) {
        if (buttons[0][i].getText().equals(p) &&
            buttons[1][i].getText().equals(p) &&
            buttons[2][i].getText().equals(p)) {

            highlightWin(buttons[0][i], buttons[1][i], buttons[2][i]);
            return true;
        }
    }

    // Diagonal 1
    if (buttons[0][0].getText().equals(p) &&
        buttons[1][1].getText().equals(p) &&
        buttons[2][2].getText().equals(p)) {

        highlightWin(buttons[0][0], buttons[1][1], buttons[2][2]);
        return true;
    }

    // Diagonal 2
    if (buttons[0][2].getText().equals(p) &&
        buttons[1][1].getText().equals(p) &&
        buttons[2][0].getText().equals(p)) {

        highlightWin(buttons[0][2], buttons[1][1], buttons[2][0]);
        return true;
    }

    return false;
}

    private boolean checkDraw() {
        for (JButton[] row : buttons) {
            for (JButton b : row) {
                if (b.getText().equals("")) return false;
            }
        }
        return true;
    }

    private void disableButtons() {
        for (JButton[] row : buttons) {
            for (JButton b : row) {
                b.setEnabled(false);
            }
        }
    }

    private void resetBoard() {
        for (JButton[] row : buttons) {
            for (JButton b : row) {
                b.setText("");
                b.setEnabled(true);
                b.setBackground(Color.darkGray);
            }
        }
        xTurn = true;
       if (vsAI && playerX.equals("Computer")) {
            status.setText("Computer Turn");
        } else {
            status.setText(playerX + " Turn");
        }
    }
    
  private void updateScore() {
    xLabel.setText(playerX + " :" + xScore);
    oLabel.setText(playerO + " :" + oScore);
    drawLabel.setText("Draw: " + drawScore);
}
    
    private void newGame() {
    xScore = 0;
    oScore = 0;
    drawScore = 0;
    updateScore();
    resetBoard();

      if (vsAI && playerX.equals("Computer")) {
            status.setText("Computer Turn");
        } else {
            status.setText(playerX + " Turn");
        }

}
   private void highlightWin(JButton b1, JButton b2, JButton b3) {
    Color hunterGreen = new Color(53, 94, 59); 

    b1.setBackground(hunterGreen);
    b2.setBackground(hunterGreen);
    b3.setBackground(hunterGreen);
}
  
  private void aiMove() {
    int bestScore = Integer.MIN_VALUE;
    int bestRow = -1;
    int bestCol = -1;

   String ai = playerX.equals("Computer") ? "X" : "O";
String human = ai.equals("X") ? "O" : "X";

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {

            if (buttons[i][j].getText().equals("")) {

                buttons[i][j].setText(ai);

                int score = minimax(false, ai, human);

                buttons[i][j].setText("");

                if (score > bestScore) {
                    bestScore = score;
                    bestRow = i;
                    bestCol = j;
                }
            }
        }
    }

   if (bestRow != -1 && bestCol != -1) {
    buttons[bestRow][bestCol].setText(ai);
}
}

   private int minimax(boolean isMaximizing, String ai, String human) {

    String winner = getWinner();
  // WIN CHECK
    if (winner != null) {
        if (winner.equals(ai)) return 10;
        if (winner.equals(human)) return -10;
        return 0;
    }
      // DRAW CHECK 
    boolean full = true;

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (buttons[i][j].getText().equals("")) {
                full = false;
                break;
            }
        }
    }
    if (full) return 0;

    if (isMaximizing) {
        int best = Integer.MIN_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(ai);
                    best = Math.max(best, minimax(false, ai, human));
                    buttons[i][j].setText("");
                }
            }
        }
        return best;
    }

    else {
        int best = Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (buttons[i][j].getText().equals("")) {
                    buttons[i][j].setText(human);
                    best = Math.min(best, minimax(true, ai, human));
                    buttons[i][j].setText("");
                }
            }
        }
        return best;
    }
}  

private String getWinner() {

    // rows
    for (int i = 0; i < 3; i++) {
        if (!buttons[i][0].getText().equals("") &&
            buttons[i][0].getText().equals(buttons[i][1].getText()) &&
            buttons[i][1].getText().equals(buttons[i][2].getText())) {
            return buttons[i][0].getText();
        }
    }

    // columns
    for (int i = 0; i < 3; i++) {
        if (!buttons[0][i].getText().equals("") &&
            buttons[0][i].getText().equals(buttons[1][i].getText()) &&
            buttons[1][i].getText().equals(buttons[2][i].getText())) {
            return buttons[0][i].getText();
        }
    }

    // diagonals
    if (!buttons[0][0].getText().equals("") &&
        buttons[0][0].getText().equals(buttons[1][1].getText()) &&
        buttons[1][1].getText().equals(buttons[2][2].getText())) {
        return buttons[0][0].getText();
    }

    if (!buttons[0][2].getText().equals("") &&
        buttons[0][2].getText().equals(buttons[1][1].getText()) &&
        buttons[1][1].getText().equals(buttons[2][0].getText())) {
        return buttons[0][2].getText();
    }

    return null;
}
    public static void main(String[] args) {
        new TicTacToe();
        setDefaultLookAndFeelDecorated(true);
    }
}