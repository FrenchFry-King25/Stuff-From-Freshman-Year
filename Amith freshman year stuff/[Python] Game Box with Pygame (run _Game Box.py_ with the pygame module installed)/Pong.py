class Pong:
    def __init__(self, window, pygame):
        self.window = window
        self.pygame = pygame

    def play(self):
        pygame = self.pygame
        window = self.window

        stage = "pre_game"
        pause = 0
        infinite = False

        large_font = pygame.font.SysFont("Gill Sans", 50)
        small_font = pygame.font.SysFont("Gill Sans", 30)

        ball = [[275, 260], [.3, -.3]]
        left_player = [20, 230, 6, 60]
        lps = 0
        right_player = [524, 230, 6, 60]
        rps = 0

        game = True
        while game:
            click = False
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    game = False
                elif event.type == pygame.MOUSEBUTTONUP:
                    click = True

            if stage == "pre_game":
                txt = "PONG"
                window.fill((0, 0, 0))
                window.blit(large_font.render(txt, True, (255, 255, 255)), (200, 10))

                pygame.draw.rect(window, (255, 255, 255), (0, 100, 550, 10))
                pygame.draw.rect(window, (255, 255, 255), (0, 410, 550, 10))

                pygame.draw.rect(window, (255, 255, 255), left_player)
                pygame.draw.rect(window, (255, 255, 255), right_player)

                txt = "Left player uses the \"w\" and \"s\" keys"
                window.blit(small_font.render(txt, True, (255, 255, 255)), (50, 430))
                txt = "Right player uses the up and down arrows"
                window.blit(small_font.render(txt, True, (255, 255, 255)), (30, 460))

                m = pygame.mouse.get_pos()
                green1 = (0, 200, 0)
                green2 = (0, 200, 0)
                if 25 <= m[0] <= 250 and 540 <= m[1] <= 590:
                    green1 = (0, 255, 0)
                if 325 <= m[0] <= 525 and 540 <= m[1] <= 590:
                    green2 = (0, 255, 0)
                pygame.draw.rect(window, green1, (25, 540, 225, 50))
                pygame.draw.rect(window, green2, (325, 540, 200, 50))
                txt = "PLAY (first to 10)"
                window.blit(small_font.render(txt, True, (255, 255, 255)), (30, 547))
                txt = "PLAY (infinite)"
                window.blit(small_font.render(txt, True, (255, 255, 255)), (330, 547))
                if click and green1:
                    stage = "game"
                if click and green2:
                    stage = "game"
                    infinite = True

            if stage == "game":
                pressed_keys = pygame.key.get_pressed()
                if pressed_keys[pygame.K_UP] and right_player[1] >= 111:
                    right_player[1] -= .2
                if pressed_keys[pygame.K_DOWN] and right_player[1] <= 349:
                    right_player[1] += .2
                if pressed_keys[pygame.K_w] and left_player[1] >= 111:
                    left_player[1] -= .2
                if pressed_keys[pygame.K_s] and left_player[1] <= 349:
                    left_player[1] += .2

                txt = str(lps) + "        " + str(rps)
                window.fill((0, 0, 0))
                window.blit(large_font.render(txt, True, (255, 255, 255)), (150, 10))
                pygame.draw.rect(window, (255, 255, 255), (0, 100, 550, 10))
                pygame.draw.rect(window, (255, 255, 255), (0, 410, 550, 10))

                pygame.draw.rect(window, (255, 255, 255), left_player)
                pygame.draw.rect(window, (255, 255, 255), right_player)

                ball[0][0] += ball[1][0]
                ball[0][1] += ball[1][1]
                pygame.draw.circle(window, (255, 255, 255), ball[0], 10)

                if ball[0][0] <= 5:
                    rps += 1
                    stage = "game_over"
                elif ball[0][0] <= 36:
                    if left_player[1] <= ball[0][1] <= (left_player[1] + 30):
                        ball[1][0] = .3
                        ball[1][1] = -.3
                    if (left_player[1] + 30) <= ball[0][1] <= (left_player[1] + 60):
                        ball[1][0] = .3
                        ball[1][1] = .3
                elif ball[0][0] >= 545:
                    lps += 1
                    stage = "game_over"
                elif ball[0][0] >= 514:
                    if right_player[1] <= ball[0][1] <= (right_player[1] + 30):
                        ball[1][0] = -.3
                        ball[1][1] = -.3
                    if (right_player[1] + 30) <= ball[0][1] <= (right_player[1] + 60):
                        ball[1][0] = -.3
                        ball[1][1] = .3

                if ball[0][1] <= 115:
                    ball[1][1] = .3
                elif ball[0][1] >= 405:
                    ball[1][1] = -.3

            if stage == "game_over":
                pressed_keys = pygame.key.get_pressed()
                if pressed_keys[pygame.K_SPACE]:
                    ball = [[275, 260], [.3, -.3]]
                    stage = "game"

            pygame.display.update()

        pygame.quit()
