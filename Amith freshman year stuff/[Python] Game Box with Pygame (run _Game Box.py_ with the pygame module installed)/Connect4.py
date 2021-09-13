class Connect4:

    def __init__(self, window, pygame):
        self.window = window
        self.pygame = pygame

    def play(self):
        pygame = self.pygame
        window = self.window

        main_list = [[".", ".", ".", ".", ".", ".", "."],
                     [".", ".", ".", ".", ".", ".", "."],
                     [".", ".", ".", ".", ".", ".", "."],
                     [".", ".", ".", ".", ".", ".", "."],
                     [".", ".", ".", ".", ".", ".", "."],
                     [".", ".", ".", ".", ".", ".", "."], ]

        player = "X"
        red_score = 0
        yellow_score = 0

        stage = "game"

        small_font = pygame.font.SysFont("Arial", 30)

        def show_main_list(main_list):
            window.fill((255, 255, 255))
            pygame.draw.rect(window, (0, 0, 255), (0, 0, 550, 468))
            for x in range(7):
                for y in range(6):
                    color = (255, 255, 255)
                    if main_list[y][x] == "X":
                        color = (255, 0, 0)
                    elif main_list[y][x] == "O":
                        color = (255, 255, 0)
                    pygame.draw.circle(window, color, ((x * 78) + 39, (y * 78) + 39), 30)

        def check_diagonal_down(main_list, player):
            for row in range(3):
                for col in range(4):
                    if main_list[5 - row][col + 3] == player:
                        if main_list[4 - row][col + 2] == player:
                            if main_list[3 - row][col + 1] == player:
                                if main_list[2 - row][col + 0] == player:
                                    return True

        def check_diagonal_up(main_list, player):
            for row in range(3):
                for col in range(4):
                    if main_list[row][6 - col] == player:
                        if main_list[row + 1][5 - col] == player:
                            if main_list[row + 2][4 - col] == player:
                                if main_list[row + 3][3 - col] == player:
                                    return True

        def check_horizontal(main_list, player):
            for row in main_list:
                for col in range(4):
                    if row[col] == player:
                        if row[col + 1] == player:
                            if row[col + 2] == player:
                                if row[col + 3] == player:
                                    return True

        def check_virtical(main_list, player):
            for col in range(7):
                for row in range(3):
                    if main_list[row][col] == player:
                        if main_list[row + 1][col] == player:
                            if main_list[row + 2][col] == player:
                                if main_list[row + 3][col] == player:
                                    return True

        game = True
        while game == True:

            click = False

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    game = False
                elif event.type == pygame.MOUSEBUTTONUP:
                    click = True
            if stage == "game":

                show_main_list(main_list)

                player_txt = ""
                if player == "X":
                    player_txt = "It's Red's turn"
                elif player == "O":
                    player_txt = "It's Yellow's turn"
                txt = player_txt
                window.blit(small_font.render(txt, True, (0, 0, 0)), (200, 547))

                m = pygame.mouse.get_pos()
                button = -1
                for i in range(7):
                    color = (0, 200, 0)
                    if ((i * 78) + 9) < m[0] < ((i * 78) + 69) and 483 < m[1] < 543:
                        color = (0, 255, 0)
                        button = i
                    pygame.draw.circle(window, color, ((i * 78) + 39, 513), 30)

                if click and button >= 0:
                    column = button
                    row = 0
                    for i in range(6):
                        spot = main_list[i][column]
                        if spot == "X" or spot == "O":
                            row += 1
                    row = 5 - row

                    main_list[row][column] = player

                    if check_diagonal_down(main_list, player) or check_diagonal_up(main_list, player) or \
                            check_horizontal(main_list, player) or check_virtical(main_list, player):
                        show_main_list(main_list)
                        if player == "X":
                            red_score += 1
                        elif player == "O":
                            yellow_score += 1
                        stage = "game_over"

                    else:
                        if player == "X":
                            player = "O"
                        elif player == "O":
                            player = "X"

            if stage == "game_over":
                pygame.draw.rect(window, (255, 255, 255), (0, 544, 550, 56))
                player_txt = ""
                if player == "X":
                    player_txt = "Red"
                elif player == "O":
                    player_txt = "Yellow"

                txt = player_txt + " is the winner!"
                window.blit(small_font.render(txt, True, (0, 0, 0)), (185, 500))
                txt = "Red:" + str(red_score) + "       Yellow: " + str(yellow_score)
                window.blit(small_font.render(txt, True, (0, 0, 0)), (100, 545))

                blue = 180
                m = pygame.mouse.get_pos()
                if 385 <= m[0] <= 510 and 540 <= m[1] <= 585:
                    blue += 75
                pygame.draw.rect(window, (0, 0, blue), (385, 540, 125, 45))
                txt = "REPLAY?"
                window.blit(small_font.render(txt, True, (255, 255, 255)), (395, 545))
                if click and blue == 255:
                    main_list = [[".", ".", ".", ".", ".", ".", "."],
                                 [".", ".", ".", ".", ".", ".", "."],
                                 [".", ".", ".", ".", ".", ".", "."],
                                 [".", ".", ".", ".", ".", ".", "."],
                                 [".", ".", ".", ".", ".", ".", "."],
                                 [".", ".", ".", ".", ".", ".", "."], ]

                    if player == "X":
                        player = "O"
                    elif player == "O":
                        player = "X"

                    stage = "game"

            pygame.display.update()

        pygame.quit()
