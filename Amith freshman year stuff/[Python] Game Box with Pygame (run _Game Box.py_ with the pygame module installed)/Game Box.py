import pygame
from TicTacToe import TicTacToe
from Connect4 import Connect4
from Pong import Pong

pyg = pygame
pyg.init()
pyg.font.init()
win = pygame.display.set_mode((550, 600))
pyg.display.set_caption("Amith's Game Box")
icon = pygame.image.load("icon.png")
pygame.display.set_icon(icon)

tttb = pyg.image.load("TTT_button.png")
c4b = pyg.image.load("C4_button.png")
pb = pyg.image.load("PONG_button.png")
sg = pyg.image.load("select_game.png")

font = pygame.font.SysFont("Harlow Solid", 55)

win.blit(tttb, (90, 120))
win.blit(c4b, (90, 280))
win.blit(pb, (90, 440))
win.blit(sg, (0, 100))
txt = "Welcome to Game Box"
win.blit(font.render(txt, True, (255, 255, 255)), (20, 20))

running = True
while running:
    pyg.display.update()

    click = False

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            running = False
        elif event.type == pygame.MOUSEBUTTONUP:
            click = True

    if click:
        m = pygame.mouse.get_pos()
        if 90 <= m[0] <= 540 and 120 <= m[1] <= 270:
            TicTacToe(win, pyg).play()
            running = False
        if 90 <= m[0] <= 540 and 280 <= m[1] <= 430:
            Connect4(win, pyg).play()
            running = False
        if 90 <= m[0] <= 540 and 440 <= m[1] <= 590:
            Pong(win, pyg).play()
            running = False

pyg.quit()
