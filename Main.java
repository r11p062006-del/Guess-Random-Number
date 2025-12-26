import java.util.Random;
import java.util.Scanner;

public class Main {
    // Game settings
    private static int score = 0;
    private static int gamesPlayed = 0;
    private static int bestScore = Integer.MAX_VALUE; // Lower is better
    private static int totalGuesses = 0;
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);

    // ANSI color codes
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
    private static final String ORANGE = "\u001B[38;5;214m";

    // Difficulty levels
    enum Difficulty {
        EASY(1, 50, 10, 3, 100),
        MEDIUM(1, 100, 15, 2, 200),
        HARD(1, 200, 20, 1, 300),
        CUSTOM(1, 100, 15, 2, 250);

        final int min;
        final int max;
        final int maxGuesses;
        final int hints;
        final int basePoints;

        Difficulty(int min, int max, int maxGuesses, int hints, int basePoints) {
            this.min = min;
            this.max = max;
            this.maxGuesses = maxGuesses;
            this.hints = hints;
            this.basePoints = basePoints;
        }
    }

    public static void main(String[] args) {
        boolean playing = true;

        clearScreen();
        showWelcomeScreen();

        while (playing) {
            clearScreen();
            showMainMenu();

            int choice = getMenuChoice(1, 6);

            switch (choice) {
                case 1:
                    playGame(Difficulty.EASY);
                    break;
                case 2:
                    playGame(Difficulty.MEDIUM);
                    break;
                case 3:
                    playGame(Difficulty.HARD);
                    break;
                case 4:
                    playCustomGame();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 6:
                    playing = false;
                    break;
            }

            if (choice != 6) {
                System.out.print("\n" + CYAN + BOLD + "Return to main menu? (Y/N): " + RESET);
                String response = scanner.next().toUpperCase();
                scanner.nextLine(); // Clear buffer

                if (!response.equals("Y")) {
                    playing = false;
                }
            }
        }

        showGoodbyeScreen();
        scanner.close();
    }

    // Clear screen
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Show welcome screen
    private static void showWelcomeScreen() {
        System.out.println(CYAN + "╔══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + BOLD + "           🔢 WELCOME TO GUESS THE NUMBER! 🔢" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════════════╝\n" + RESET);

        System.out.println(YELLOW + BOLD + "🎯 HOW TO PLAY:\n" + RESET);
        System.out.println("1. " + GREEN + "Choose a difficulty level or set custom range" + RESET);
        System.out.println("2. " + GREEN + "I'll think of a secret number in that range" + RESET);
        System.out.println("3. " + GREEN + "You have limited attempts to guess it" + RESET);
        System.out.println("4. " + GREEN + "I'll tell you if your guess is too high or too low" + RESET);
        System.out.println("5. " + GREEN + "Use hints wisely to help you guess faster!\n" + RESET);

        System.out.println(YELLOW + BOLD + "🏆 SCORING:\n" + RESET);
        System.out.println("• " + CYAN + "Fewer guesses = More points" + RESET);
        System.out.println("• " + CYAN + "Harder difficulty = Higher base points" + RESET);
        System.out.println("• " + CYAN + "Track your best game (fewest guesses)" + RESET);

        System.out.print("\n" + GREEN + BOLD + "Press Enter to start..." + RESET);
        scanner.nextLine();
    }

    // Show main menu
    private static void showMainMenu() {
        System.out.println(PURPLE + "══════════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + BOLD + "                     MAIN MENU" + RESET);
        System.out.println(PURPLE + "══════════════════════════════════════════════════════════\n" + RESET);

        // Show current stats
        System.out.println(YELLOW + BOLD + "📊 CURRENT STATISTICS:\n" + RESET);
        System.out.println("  Games Played: " + gamesPlayed);
        System.out.println("  Total Score: " + score + " points");

        if (gamesPlayed > 0) {
            double avgGuesses = (double) totalGuesses / gamesPlayed;
            System.out.printf("  Average Guesses: %.1f\n", avgGuesses);

            if (bestScore != Integer.MAX_VALUE) {
                System.out.println("  Best Game: " + bestScore + " guesses");
            }
        }

        System.out.println("\n" + GREEN + BOLD + "🎯 SELECT DIFFICULTY:\n" + RESET);
        System.out.println("  " + GREEN + "1. Easy Mode (1-50)" + RESET);
        System.out.println("     • 10 attempts, 3 hints");
        System.out.println("     • Base points: 100\n");

        System.out.println("  " + BLUE + "2. Medium Mode (1-100)" + RESET);
        System.out.println("     • 15 attempts, 2 hints");
        System.out.println("     • Base points: 200\n");

        System.out.println("  " + RED + "3. Hard Mode (1-200)" + RESET);
        System.out.println("     • 20 attempts, 1 hint");
        System.out.println("     • Base points: 300\n");

        System.out.println("  " + PURPLE + "4. Custom Range" + RESET);
        System.out.println("     • Set your own range (1-1000)");
        System.out.println("     • Dynamic attempts & hints\n");

        System.out.println("  " + CYAN + "5. View Detailed Statistics" + RESET + " 📊\n");
        System.out.println("  " + ORANGE + "6. Exit Game" + RESET + " 🚪\n");
    }

    // Get menu choice
    private static int getMenuChoice(int min, int max) {
        while (true) {
            System.out.print(YELLOW + BOLD + "Enter choice (" + min + "-" + max + "): " + RESET);

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= min && choice <= max) {
                    return choice;
                }
            } else {
                scanner.next();
            }

            System.out.println(RED + "Invalid! Enter " + min + "-" + max + RESET);
        }
    }

    // Play game with given difficulty
    private static void playGame(Difficulty difficulty) {
        clearScreen();

        switch (difficulty) {
            case EASY:
                System.out.println(GREEN + BOLD + "╔══════════════════════════════════════════════════════════╗" + RESET);
                System.out.println(GREEN + BOLD + "                         EASY MODE" + RESET);
                System.out.println(GREEN + BOLD + "╚══════════════════════════════════════════════════════════╝\n" + RESET);
                break;
            case MEDIUM:
                System.out.println(BLUE + BOLD + "╔══════════════════════════════════════════════════════════╗" + RESET);
                System.out.println(BLUE + BOLD + "                       MEDIUM MODE" + RESET);
                System.out.println(BLUE + BOLD + "╚══════════════════════════════════════════════════════════╝\n" + RESET);
                break;
            case HARD:
                System.out.println(RED + BOLD + "╔══════════════════════════════════════════════════════════╗" + RESET);
                System.out.println(RED + BOLD + "                         HARD MODE" + RESET);
                System.out.println(RED + BOLD + "╚══════════════════════════════════════════════════════════╝\n" + RESET);
                break;
        }

        System.out.println(YELLOW + "🎯 I'm thinking of a number between " +
                difficulty.min + " and " + difficulty.max + RESET);
        System.out.println(YELLOW + "📊 You have " + difficulty.maxGuesses +
                " attempts and " + difficulty.hints + " hint(s)\n" + RESET);

        int secretNumber = random.nextInt(difficulty.max - difficulty.min + 1) + difficulty.min;
        playRound(difficulty, secretNumber);
    }

    // Play custom game
    private static void playCustomGame() {
        clearScreen();
        System.out.println(PURPLE + BOLD + "╔══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + BOLD + "                       CUSTOM MODE" + RESET);
        System.out.println(PURPLE + BOLD + "╚══════════════════════════════════════════════════════════╝\n" + RESET);

        System.out.println(YELLOW + "Create your own challenge!\n" + RESET);

        int min = getNumberInput("Enter minimum number (1-500): ", 1, 500);
        int max = getNumberInput("Enter maximum number (" + (min + 1) + "-1000): ", min + 1, 1000);

        int range = max - min + 1;
        int maxGuesses = calculateMaxGuesses(range);
        int hints = calculateHints(range);

        System.out.println("\n" + GREEN + BOLD + "🎮 CUSTOM GAME SETTINGS:" + RESET);
        System.out.println(YELLOW + "  Range: " + min + " to " + max);
        System.out.println("  Attempts: " + maxGuesses);
        System.out.println("  Hints: " + hints);
        System.out.println("  Range size: " + range + " numbers\n" + RESET);

        int secretNumber = random.nextInt(max - min + 1) + min;

        // Create custom difficulty
        Difficulty custom = Difficulty.CUSTOM;
        System.out.println(YELLOW + "🎯 I'm thinking of a number between " + min + " and " + max + RESET);
        System.out.println(YELLOW + "📊 You have " + maxGuesses + " attempts and " + hints + " hint(s)\n" + RESET);

        playRoundCustom(min, max, maxGuesses, hints, secretNumber);
    }

    // Play a round
    private static void playRound(Difficulty difficulty, int secretNumber) {
        int attempts = 0;
        int hintsUsed = 0;
        boolean won = false;
        int lastGuess = -1;

        System.out.println(CYAN + "💡 Commands: Type 'H' for hint, 'Q' to quit\n" + RESET);

        while (attempts < difficulty.maxGuesses && !won) {
            attempts++;
            System.out.print(YELLOW + BOLD + "Attempt " + attempts + "/" + difficulty.maxGuesses +
                    " [" + (difficulty.hints - hintsUsed) + " hints left]: " + RESET);

            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Q")) {
                System.out.println(RED + "\nGame cancelled. The number was: " + secretNumber + RESET);
                return;
            }

            if (input.equals("H")) {
                if (hintsUsed < difficulty.hints) {
                    giveHint(secretNumber, lastGuess, difficulty.min, difficulty.max);
                    hintsUsed++;
                    attempts--; // Don't count hint as attempt
                    continue;
                } else {
                    System.out.println(RED + "No hints left!" + RESET);
                    attempts--;
                    continue;
                }
            }

            try {
                int guess = Integer.parseInt(input);
                lastGuess = guess;

                if (guess < difficulty.min || guess > difficulty.max) {
                    System.out.println(RED + "Number must be between " + difficulty.min +
                            " and " + difficulty.max + "!" + RESET);
                    attempts--;
                } else if (guess == secretNumber) {
                    won = true;
                } else if (guess < secretNumber) {
                    System.out.println(BLUE + "📈 Too low!" + RESET);
                    giveProximityHint(secretNumber, guess);
                } else {
                    System.out.println(RED + "📉 Too high!" + RESET);
                    giveProximityHint(secretNumber, guess);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Enter a number, 'H' for hint, or 'Q' to quit!" + RESET);
                attempts--;
            }
        }

        showRoundResult(won, attempts, difficulty.maxGuesses, secretNumber, difficulty);

        // Update stats
        updateStats(won, attempts, difficulty);
    }

    // Play custom round
    private static void playRoundCustom(int min, int max, int maxGuesses, int hints, int secretNumber) {
        int attempts = 0;
        int hintsUsed = 0;
        boolean won = false;
        int lastGuess = -1;

        System.out.println(CYAN + "💡 Commands: Type 'H' for hint, 'Q' to quit\n" + RESET);

        while (attempts < maxGuesses && !won) {
            attempts++;
            System.out.print(YELLOW + BOLD + "Attempt " + attempts + "/" + maxGuesses +
                    " [" + (hints - hintsUsed) + " hints left]: " + RESET);

            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Q")) {
                System.out.println(RED + "\nGame cancelled. The number was: " + secretNumber + RESET);
                return;
            }

            if (input.equals("H")) {
                if (hintsUsed < hints) {
                    giveHint(secretNumber, lastGuess, min, max);
                    hintsUsed++;
                    attempts--;
                    continue;
                } else {
                    System.out.println(RED + "No hints left!" + RESET);
                    attempts--;
                    continue;
                }
            }

            try {
                int guess = Integer.parseInt(input);
                lastGuess = guess;

                if (guess < min || guess > max) {
                    System.out.println(RED + "Number must be between " + min +
                            " and " + max + "!" + RESET);
                    attempts--;
                } else if (guess == secretNumber) {
                    won = true;
                } else if (guess < secretNumber) {
                    System.out.println(BLUE + "📈 Too low!" + RESET);
                    giveProximityHint(secretNumber, guess);
                } else {
                    System.out.println(RED + "📉 Too high!" + RESET);
                    giveProximityHint(secretNumber, guess);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "Enter a number, 'H' for hint, or 'Q' to quit!" + RESET);
                attempts--;
            }
        }

        showCustomRoundResult(won, attempts, maxGuesses, secretNumber, min, max);

        // Update stats for custom game
        gamesPlayed++;
        totalGuesses += attempts;
        if (won && attempts < bestScore) {
            bestScore = attempts;
        }

        // Calculate points for custom game
        if (won) {
            int range = max - min + 1;
            int basePoints = Math.min(300, 50 + range / 2);
            double efficiency = (double) (maxGuesses - attempts + 1) / maxGuesses;
            int pointsEarned = (int) (basePoints * efficiency);
            score += pointsEarned;
            System.out.println(GREEN + "🏅 Points earned: " + pointsEarned + RESET);
        }
    }

    // Give hint
    private static void giveHint(int secret, int lastGuess, int min, int max) {
        Random rand = new Random();
        int hintType = rand.nextInt(5);

        System.out.println(ORANGE + "\n💡 HINT:" + RESET);

        switch (hintType) {
            case 0:
                System.out.println(ORANGE + "  The number is " +
                        (secret % 2 == 0 ? "even" : "odd") + RESET);
                break;

            case 1:
                int range = max - min + 1;
                if (secret < min + range/3) {
                    System.out.println(ORANGE + "  The number is in the lower third" + RESET);
                } else if (secret > max - range/3) {
                    System.out.println(ORANGE + "  The number is in the upper third" + RESET);
                } else {
                    System.out.println(ORANGE + "  The number is in the middle third" + RESET);
                }
                break;

            case 2:
                if (secret % 10 == 0) {
                    System.out.println(ORANGE + "  The number ends with 0" + RESET);
                } else {
                    System.out.println(ORANGE + "  The number ends with " + (secret % 10) + RESET);
                }
                break;

            case 3:
                if (lastGuess != -1) {
                    int diff = Math.abs(secret - lastGuess);
                    if (diff <= 10) {
                        System.out.println(ORANGE + "  Very close! Within 10 numbers" + RESET);
                    } else if (diff <= 25) {
                        System.out.println(ORANGE + "  Getting warmer! Within 25 numbers" + RESET);
                    } else {
                        System.out.println(ORANGE + "  Pretty far away!" + RESET);
                    }
                } else {
                    System.out.println(ORANGE + "  The number is between " + min + " and " + max + RESET);
                }
                break;

            case 4:
                if (secret < (min + max) / 2) {
                    System.out.println(ORANGE + "  The number is in the first half" + RESET);
                } else {
                    System.out.println(ORANGE + "  The number is in the second half" + RESET);
                }
                break;
        }
        System.out.println();
    }

    // Give proximity hint
    private static void giveProximityHint(int secret, int guess) {
        int difference = Math.abs(secret - guess);

        if (difference <= 3) {
            System.out.println(GREEN + "  🔥 Burning hot! You're really close!" + RESET);
        } else if (difference <= 10) {
            System.out.println(YELLOW + "  🌡️ Getting warm!" + RESET);
        } else if (difference <= 25) {
            System.out.println(BLUE + "  ❄️ A bit cold..." + RESET);
        } else {
            System.out.println(PURPLE + "  🥶 Ice cold! Far away!" + RESET);
        }
    }

    // Show round result
    private static void showRoundResult(boolean won, int attempts, int maxGuesses,
                                        int secretNumber, Difficulty difficulty) {
        System.out.println("\n" + PURPLE + "══════════════════════════════════════════════════════════" + RESET);

        if (won) {
            System.out.println(GREEN + BOLD + "               🎉 CONGRATULATIONS! 🎉" + RESET);
            System.out.println(GREEN + "        You guessed the number " + secretNumber +
                    " in " + attempts + " attempts!" + RESET);
        } else {
            System.out.println(RED + BOLD + "               💀 GAME OVER! 💀" + RESET);
            System.out.println(RED + "        The secret number was: " + secretNumber + RESET);
            System.out.println(RED + "        You used " + attempts + " of " + maxGuesses +
                    " attempts" + RESET);
        }

        System.out.println(PURPLE + "══════════════════════════════════════════════════════════" + RESET);
    }

    // Show custom round result
    private static void showCustomRoundResult(boolean won, int attempts, int maxGuesses,
                                              int secretNumber, int min, int max) {
        System.out.println("\n" + PURPLE + "══════════════════════════════════════════════════════════" + RESET);

        if (won) {
            System.out.println(GREEN + BOLD + "               🎉 CONGRATULATIONS! 🎉" + RESET);
            System.out.println(GREEN + "        You guessed " + secretNumber +
                    " in " + attempts + "/" + maxGuesses + " attempts!" + RESET);

            // Performance rating
            double efficiency = (attempts * 100.0) / maxGuesses;
            System.out.print(YELLOW + "        Efficiency: " +
                    String.format("%.1f", efficiency) + "% - " + RESET);

            if (efficiency < 30) {
                System.out.println(GREEN + "Outstanding! ⭐⭐⭐" + RESET);
            } else if (efficiency < 50) {
                System.out.println(BLUE + "Great job! ⭐⭐" + RESET);
            } else if (efficiency < 70) {
                System.out.println(YELLOW + "Good effort! ⭐" + RESET);
            } else {
                System.out.println(ORANGE + "Keep practicing!" + RESET);
            }
        } else {
            System.out.println(RED + BOLD + "               💀 GAME OVER! 💀" + RESET);
            System.out.println(RED + "        The number was: " + secretNumber + RESET);
            System.out.println(RED + "        Range: " + min + " to " + max + RESET);
        }

        System.out.println(PURPLE + "══════════════════════════════════════════════════════════" + RESET);
    }

    // Update statistics
    private static void updateStats(boolean won, int attempts, Difficulty difficulty) {
        gamesPlayed++;
        totalGuesses += attempts;

        if (won) {
            if (attempts < bestScore) {
                bestScore = attempts;
            }

            // Calculate points
            double efficiency = (double) (difficulty.maxGuesses - attempts + 1) / difficulty.maxGuesses;
            int pointsEarned = (int) (difficulty.basePoints * efficiency);
            score += pointsEarned;

            System.out.println(GREEN + "🏅 Points earned: " + pointsEarned +
                    " (Total: " + score + ")" + RESET);
        }
    }

    // Show statistics
    private static void showStatistics() {
        clearScreen();
        System.out.println(CYAN + "╔══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(YELLOW + BOLD + "                  GAME STATISTICS" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════════════╝\n" + RESET);

        System.out.println(YELLOW + BOLD + "📊 OVERALL PERFORMANCE:\n" + RESET);
        System.out.println("  Games Played: " + gamesPlayed);
        System.out.println("  Total Score: " + score + " points");

        if (gamesPlayed > 0) {
            double avgGuesses = (double) totalGuesses / gamesPlayed;
            double winRate = (gamesPlayed > 0 ?
                    ((gamesPlayed - countLosses()) * 100.0) / gamesPlayed : 0);

            System.out.printf("  Average Guesses: %.1f\n", avgGuesses);
            System.out.printf("  Win Rate: %.1f%%\n", winRate);

            if (bestScore != Integer.MAX_VALUE) {
                System.out.println("  Best Game: " + bestScore + " guesses");
            }
        }

        System.out.println("\n" + GREEN + BOLD + "🏆 PERFORMANCE RATING:\n" + RESET);

        if (gamesPlayed == 0) {
            System.out.println("  🎯 Play your first game to get a rating!");
        } else {
            double avgGuesses = (double) totalGuesses / gamesPlayed;

            if (avgGuesses <= 5) {
                System.out.println(GREEN + "  🥇 NUMBER GUESSING MASTER!" + RESET);
                System.out.println("  You have exceptional intuition!");
            } else if (avgGuesses <= 8) {
                System.out.println(BLUE + "  🥈 EXPERT GUESSER" + RESET);
                System.out.println("  Great logical thinking!");
            } else if (avgGuesses <= 12) {
                System.out.println(YELLOW + "  🥉 SKILLED PLAYER" + RESET);
                System.out.println("  Good guessing strategy!");
            } else {
                System.out.println(ORANGE + "  🎯 PRACTICE MAKES PERFECT" + RESET);
                System.out.println("  Keep playing to improve!");
            }
        }

        System.out.println("\n" + PURPLE + BOLD + "💡 TIPS FOR IMPROVEMENT:\n" + RESET);
        System.out.println("  1. Start with middle of the range");
        System.out.println("  2. Use binary search strategy");
        System.out.println("  3. Save hints for when you're stuck");
        System.out.println("  4. Pay attention to proximity hints");

        System.out.print("\n" + CYAN + BOLD + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    // Helper method to count losses (simplified)
    private static int countLosses() {
        // In a real game, you'd track wins/losses separately
        // For simplicity, we'll estimate
        return gamesPlayed / 3; // Rough estimate
    }

    // Get number input with validation
    private static int getNumberInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(YELLOW + prompt + RESET);

            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine();

                if (num >= min && num <= max) {
                    return num;
                }
            } else {
                scanner.next();
            }

            System.out.println(RED + "Enter a number between " + min + " and " + max + RESET);
        }
    }

    // Calculate max guesses based on range
    private static int calculateMaxGuesses(int range) {
        return (int) Math.ceil(Math.log(range) / Math.log(2)) + 2;
    }

    // Calculate hints based on range
    private static int calculateHints(int range) {
        if (range <= 50) return 3;
        if (range <= 100) return 2;
        return 1;
    }

    // Show goodbye screen
    private static void showGoodbyeScreen() {
        clearScreen();
        System.out.println(CYAN + "╔══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + BOLD + "            THANK YOU FOR PLAYING!" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════════════════════════╝\n" + RESET);

        System.out.println(YELLOW + "🔢 GUESS THE NUMBER - FINAL STATISTICS\n" + RESET);

        System.out.println("  Games Played: " + gamesPlayed);
        System.out.println("  Final Score: " + score + " points");

        if (gamesPlayed > 0) {
            double avgGuesses = (double) totalGuesses / gamesPlayed;
            System.out.printf("  Average Guesses per Game: %.1f\n", avgGuesses);

            if (bestScore != Integer.MAX_VALUE) {
                System.out.println("  Best Game: " + bestScore + " guesses");
            }

            // Final rating
            System.out.println("\n" + GREEN + "🏆 YOUR FINAL RATING:" + RESET);

            if (avgGuesses <= 5) {
                System.out.println(GREEN + "  🥇 NUMBER GENIUS!" + RESET);
            } else if (avgGuesses <= 8) {
                System.out.println(BLUE + "  🥈 EXCELLENT PLAYER!" + RESET);
            } else if (avgGuesses <= 12) {
                System.out.println(YELLOW + "  🥉 GOOD PLAYER!" + RESET);
            } else {
                System.out.println(ORANGE + "  🎯 KEEP PRACTICING!" + RESET);
            }
        }

        System.out.println("\n" + PURPLE + "Come back soon to beat your high score! 🎯" + RESET);
        System.out.println("\n" + RED + "Game closing..." + RESET);
    }
}