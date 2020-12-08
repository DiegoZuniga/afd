jQuery(document).ready(function() {

    var winnerSquarePlayer1 = 16;
    var winnerSquarePlayer2 = 13;

    // Winners file path
    var player1Plays = [];
    var player2Plays = [];

    var squaresMap = [];

    squaresMap.push("1-10-10");
    squaresMap.push("2-80-10");
    squaresMap.push("3-150-10");
    squaresMap.push("4-220-10");
    squaresMap.push("5-10-80");
    squaresMap.push("6-80-80");
    squaresMap.push("7-150-80");
    squaresMap.push("8-220-80");
    squaresMap.push("9-10-150");
    squaresMap.push("10-80-150");
    squaresMap.push("11-150-150");
    squaresMap.push("12-220-150");
    squaresMap.push("13-10-220");
    squaresMap.push("14-80-220");
    squaresMap.push("15-150-220");
    squaresMap.push("16-220-220");

    jQuery("#plays_input textarea").val("");

    jQuery("#play").click(function() {
        var plays = jQuery("#plays_input textarea").val();

        if (plays != undefined && plays.length > 0) {
            jQuery(".piece1").delay(10).animate({ left: "10px", top: "60px" }, 500).delay(500);
            jQuery(".piece2").delay(10).animate({ left: "220px", top: "60px" }, 500).delay(500);

            jQuery(".player1wins").fadeOut("fast", function() {});
            jQuery(".player2wins").fadeOut("fast", function() {});

            var rows = plays.split("\n");
            var contPlayer1 = 0;
            var contPlayer2 = 0;
            for (var i = 0, len = rows.length; i < len; i++) {
                var play = rows[i].split(", ");
                if (play[0] == 1) {
                    player1Plays[contPlayer1++] = rows[i];
                } else if (play[0] == 4) {
                    player2Plays[contPlayer2++] = rows[i];
                }
            }

            var invalidPlay = true;
            var play1;
            var play2;
            while (invalidPlay) {
                play1 = player1Plays[Math.floor(Math.random() * player1Plays.length)];
                play2 = player2Plays[Math.floor(Math.random() * player2Plays.length)];

                invalidPlay = validatePlays(play1, play2);
            }

            var starterPlayer = Math.floor(Math.random() * 2);

            playGame(starterPlayer, play1, play2);
        }
    });

    function validatePlays(play1, play2) {
        var invalidPlay = false;
        var play1Split = play1.split(", ");
        var play2Split = play2.split(", ");
        for (var i = 0; i < play1Split.length; i++) {
            if (play1Split[i] == play2Split[i]) {
                invalidPlay = true;
            }
        }
        return invalidPlay;
    }

    function playGame(starterPlayer, play1, play2) {
        play1 = play1.split(", ");
        play2 = play2.split(", ");
        var notWinner = true;
        var nextMove = starterPlayer;

        var contPlay1 = 0;
        var contPlay2 = 0;
        var contMoves = 0;
        while (notWinner) {
            contMoves++;
            var actualSquare;
            if (nextMove == 0) {
                actualSquare = movePiece1(play1[++contPlay1], contMoves);
                if (actualSquare == winnerSquarePlayer1) {
                    notWinner = false;
                    jQuery(".player1wins").delay(5000).fadeIn("slow", function() {});
                }
                nextMove = 1;
            } else if (nextMove == 1) {
                actualSquare = movePiece2(play2[++contPlay2], contMoves);
                if (actualSquare == winnerSquarePlayer2) {
                    notWinner = false;
                    jQuery(".player2wins").delay(5000).fadeIn("slow", function() {});
                }
                nextMove = 0;
            }
        }
    }

    function movePiece1(square, moves) {
        var coords = squaresMap[square - 1].split("-");
        var left = coords[1] + "px";
        var top = parseInt(coords[2]) + 50;
        top = top + "px";
        if (moves == 2) {
            jQuery(".piece1").delay(500).animate({ left: left, top: top }, 500).delay(500);
        } else {
            jQuery(".piece1").animate({ left: left, top: top }, 500).delay(500);
        }
        return square;
    }

    function test() {

    }

    function movePiece2(square, moves) {
        var coords = squaresMap[square - 1].split("-");
        var left = coords[1] + "px";
        var top = parseInt(coords[2]) + 50;
        top = top + "px";
        if (moves == 2) {
            jQuery(".piece2").delay(500).animate({ left: left, top: top }, 500).delay(500);
        } else {
            jQuery(".piece2").animate({ left: left, top: top }, 500).delay(500);
        }
        return square;
    }

});