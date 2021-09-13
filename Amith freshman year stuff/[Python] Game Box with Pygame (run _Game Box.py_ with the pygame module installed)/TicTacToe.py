class TicTacToe:

    player = "x"
    board = [["", "", ""],
             ["", "", ""],
             ["", "", ""]]  # "x" = X, "o" = O, "" = blank

    stage = "starting"

    x_score = 0
    o_score = 0

    def __init__(self, window, pygame):
        self.window = window
        self.pygame = pygame

    def play(self):
        global stage, x_score, o_score, player, board
        pygame = self.pygame
        window = self.window

        starting = pygame.image.load('TTT_ss.png')

        large_font = pygame.font.SysFont("Arial", 50)
        small_font = pygame.font.SysFont("Arial", 30)

        winner = "tie"

        player = "x"
        board = [["", "", ""],
                 ["", "", ""],
                 ["", "", ""]]  # "x" = X, "o" = O, "" = blank

        stage = "starting"

        x_score = 0
        o_score = 0

        #            0 red       1 orange       2 yellow       3 green      4 blue       5 purple
        colors = [(255, 0, 0), (255, 165, 0), (255, 255, 0), (0, 255, 0), (0, 0, 255), (255, 0, 255)]
        x_color = 1
        o_color = 4

        def draw_squares():
            window.fill((0, 0, 0))
            for x in range(3):
                for y in range(3):
                    pygame.draw.rect(window, (255, 255, 255), (25 + (x * 175), 25 + (y * 175), 150, 150))

        def draw_x(x, y):
            xSet = (x * 175) + 25
            ySet = (y * 175) + 25
            pygame.draw.polygon(window, colors[x_color], [(15 + xSet, 35 + ySet),
                                                          (35 + xSet, 15 + ySet),
                                                          (135 + xSet, 115 + ySet),
                                                          (115 + xSet, 135 + ySet)])
            pygame.draw.polygon(window, colors[x_color], [(115 + xSet, 15 + ySet),
                                                          (135 + xSet, 35 + ySet),
                                                          (35 + xSet, 135 + ySet),
                                                          (15 + xSet, 115 + ySet)])

        def draw_o(x, y):
            xSet = (x * 175) + 25
            ySet = (y * 175) + 25
            pygame.draw.circle(window, colors[o_color], (75 + xSet, 75 + ySet), 60, 25)

        def draw_board():
            for x in range(3):
                for y in range(3):
                    if board[x][y] == "x":
                        draw_x(x, y)
                    elif board[x][y] == "o":
                        draw_o(x, y)

        def change_players():
            global player
            if player == "x":
                player = "o"
            elif player == "o":
                player = "x"

        def out_of_moves():
            blank = True
            for x in range(3):
                for y in range(3):
                    if board[x][y] == "":
                        blank = False
                        break
            return blank

        def tiar():
            for i in range(3):
                if board[i][0] != "" and board[i][0] == board[i][1] == board[i][2]:
                    pygame.draw.rect(window, (0, 0, 0), (25 + 67 + (i * 175), 50, 16, 450))
                    return board[i][0]
            for i in range(3):
                if board[0][i] != "" and board[0][i] == board[1][i] == board[2][i]:
                    pygame.draw.rect(window, (0, 0, 0), (50, 25 + 67 + (i * 175), 450, 16))
                    return board[0][i]
            if board[0][0] != "" and board[0][0] == board[1][1] == board[2][2]:
                pygame.draw.polygon(window, (0, 0, 0), [(70, 90), (90, 70), (480, 460), (460, 480)])
                return board[0][0]
            if board[2][0] != "" and board[2][0] == board[1][1] == board[0][2]:
                pygame.draw.polygon(window, (0, 0, 0), [(70, 460), (90, 480), (480, 90), (460, 70)])
                return board[2][0]

            return "neither"

        def game_stage(click):
            draw_squares()

            if click:
                cell = [-1, -1]
                m = pygame.mouse.get_pos()
                for i in range(2):
                    if 25 <= m[i] <= 175:
                        cell[i] = 0
                    elif 200 <= m[i] <= 350:
                        cell[i] = 1
                    elif 375 <= m[i] <= 525:
                        cell[i] = 2
                if cell[0] >= 0 and cell[1] >= 0:
                    if board[cell[0]][cell[1]] == "":
                        board[cell[0]][cell[1]] = player
                        change_players()
                        # print(board)

            draw_board()
            pygame.draw.rect(window, (0, 0, 0), (0, 525, 550, 75))
            txt = player.upper() + " 's turn"
            window.blit(large_font.render(txt, True, (255, 255, 255)), (210, 530))

            global x_score, o_score
            s = tiar()
            if s == "x":
                x_score += 1
                return s
            elif s == "o":
                o_score += 1
                return s
            elif out_of_moves():
                return "tie"

            return "keep going"

        def new_game():
            global board, stage
            board = [["", "", ""],
                     ["", "", ""],
                     ["", "", ""]]
            stage = "play_game"

        # main loop
        running = True
        while running:

            click = False

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    running = False
                elif event.type == pygame.MOUSEBUTTONUP:
                    click = True

            if stage == "starting":
                window.fill((0, 0, 0))
                window.blit(starting, (0, 0))
                for i in range(3):
                    pygame.draw.polygon(window, colors[i],
                                        [(0 + 102, 35 + 200 + (i * 110)), (20 + 102, 15 + 200 + (i * 110)),
                                         (80 + 102, 75 + 200 + (i * 110)), (60 + 102, 95 + 200 + (i * 110))])
                    pygame.draw.polygon(window, colors[i],
                                        [(0 + 102, 75 + 200 + (i * 110)), (20 + 102, 95 + 200 + (i * 110)),
                                         (80 + 102, 35 + 200 + (i * 110)), (60 + 102, 15 + 200 + (i * 110))])
                for i in range(3):
                    pygame.draw.circle(window, colors[i + 3], (417, 255 + (i * 110)), 40, 20)
                m = pygame.mouse.get_pos()
                green = (0, 200, 0)
                if 200 <= m[0] <= 350 and 540 <= m[1] <= 590:
                    green = (0, 255, 0)
                if click:
                    if green[1] == 255:
                        stage = "play_game"
                    elif 102 <= m[0] <= 183:
                        if 215 <= m[1] <= 295:
                            x_color = 0
                        elif 325 <= m[1] <= 405:
                            x_color = 1
                        elif 435 <= m[1] <= 515:
                            x_color = 2
                    elif 377 <= m[0] <= 458:
                        if 215 <= m[1] <= 295:
                            o_color = 3
                        elif 325 <= m[1] <= 405:
                            o_color = 4
                        elif 435 <= m[1] <= 515:
                            o_color = 5
                pygame.draw.rect(window, (255, 255, 255), (87, 200 + (x_color * 110), 110, 110), 10)
                pygame.draw.rect(window, (255, 255, 255), (362, 200 + ((o_color - 3) * 110), 110, 110), 10)
                pygame.draw.rect(window, green, (200, 540, 150, 50))
                txt = "PLAY"
                window.blit(small_font.render(txt, True, (255, 255, 255)), (247, 547))
            if stage == "play_game":
                s = game_stage(click)
                if s == "x" or s == "o":
                    stage = "game_over"
                    winner = s.upper() + " is the Winner!"
                elif s == "tie":
                    stage = "game_over"
                    winner = "Tie Game"
            if stage == "game_over":
                pygame.draw.rect(window, (0, 0, 0), (0, 525, 550, 75))
                txt = winner
                window.blit(small_font.render(txt, True, (255, 255, 255)), (50, 530))
                txt = "   X: " + str(x_score) + "     O: " + str(o_score)
                window.blit(small_font.render(txt, True, (255, 255, 255)), (50, 560))
                blue = 180
                m = pygame.mouse.get_pos()
                if 385 <= m[0] <= 510 and 540 <= m[1] <= 585:
                    blue += 75
                pygame.draw.rect(window, (0, 0, blue), (385, 540, 125, 45))
                txt = "REPLAY?"
                window.blit(small_font.render(txt, True, (255, 255, 255)), (395, 545))
                if click and blue == 255:
                    new_game()

            pygame.display.update()

        pygame.quit()
