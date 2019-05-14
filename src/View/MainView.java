package View;


import controller.ChessBoard;
import controller.Controller;
import controller.ViewPiece;
import controller.ViewState;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Pos;

import java.util.Set;

public class MainView extends Application{
    private GridPane chessBoardPane;
    private Text whoseTurnText = new Text();
    private Text totalRoundText = new Text();
    private Square[][] squares = new Square[8][8]; //view
    private Controller controller;

    public void start(Stage primaryStage){
        chessBoardPane = new GridPane();
        boolean whiteSquare = true;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j< 8; j++){
                Square square = new Square();
                squares[i][j] = square;
                square.setRowCol(i, j);
                square.setColor(whiteSquare);
                if(j != 7){
                    whiteSquare = (!whiteSquare);
                }

                //be careful: j and i are exchanged
                chessBoardPane.add(square, j, i);
            }
        }

        //initiate the chess board
        controller = new Controller();
        drawBoard(controller.clickHandler(new Pos(4,4)));
        HBox infoView = new HBox(50);
        infoView.getChildren().addAll(whoseTurnText,totalRoundText);
        //The scene is a border pane, which contains a center gridPane and a bottom text.
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(chessBoardPane);
        borderPane.setPadding(new Insets(10,10,10,10));
        borderPane.setBottom(infoView);

        Scene scene = new Scene(borderPane, 600, 600);
        primaryStage.setTitle("Chess Game");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    public void clearBoard() {
        boolean whiteSquare = true;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j< 8; j++){
                squares[i][j].setColor(whiteSquare);
                squares[i][j].resetText("");
                if(j != 7){
                    whiteSquare = (!whiteSquare);
                }
            }
        }

    }

    public void drawBoard(ViewState viewState){
        clearBoard();
        for (Pos pos: viewState.getPossibleMoves()) {
            squares[pos.getRow()][pos.getCol()].setStyle("-fx-border-color: Green");
        }
        for(ViewPiece viewPiece: viewState.getPieces()){
            squares[viewPiece.getPos().getRow()][viewPiece.getPos().getCol()].drawPiece(viewPiece.getId());
        }
        if ("WB".contains(viewState.getWhoseTurn())) {
            whoseTurnText.setText("Whose Turn: "+viewState.getWhoseTurn());
        } else {
            whoseTurnText.setText(viewState.getWhoseTurn());
        }
        totalRoundText.setText("Total round: "+ viewState.getTotalRound());

    }



    public static void main(String[] args) {
        launch(args);
    }

    private class Square extends StackPane{
        //		private boolean isWhite = true;
        private int row, col;
        private Text text = new Text();
        //		private Piece piece;
        private Square(){
            setPrefSize(60, 60);
            setStyle("-fx-border-color: black");
            setOnMouseClicked(e -> handleMouseClick());
        }

        public void setRowCol(int row, int col){
            this.row = row;
            this.col = col;
        }
        public int getRow(){
            return row;
        }
        public int getCol(){
            return col;
        }
        public void setColor(boolean isWhite){
            if(isWhite){
                setStyle("-fx-background-color: burlywood");

            }else{
                setStyle("-fx-background-color: darkgoldenrod");

            }
        }
        private void handleMouseClick(){
            //use row, col and piece;
            drawBoard(controller.clickHandler(new Pos(row, col)));
            //draw chessBoardPane

        }
        public void drawPiece(String id){
            text = new Text(id);
            text.setFont(Font.font(50));
            text.setTextAlignment(TextAlignment.CENTER);
            if("KQNBPR".contains(id)) {
                text.setFill(Color.WHITE);
            }else{
                text.setFill(Color.BLACK);
            }

            this.getChildren().add(text);

            this.setAlignment(javafx.geometry.Pos.CENTER);
        }
        public void resetText(String id){
            text.setText(id);
        }
    }
}

