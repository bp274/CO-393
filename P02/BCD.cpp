#include <iostream>
#include <graphics.h>
#include <conio.h>

using namespace std;

void drawFlipFlop(int x, int y, int len, int wid, int wire, char* ch)
{
    rectangle(x, y, x + len, y + wid);
    outtextxy(x + len / 2 - 2, y + wid / 2 - 7, ch);

    outtextxy(x + 5, y + wid / 5, "S");
    line(x - wire, y + wid / 4, x, y + wid / 4);

    outtextxy(x + 5, y + 3 * wid / 5 + 10, "R");
    line(x - wire, y + 4 * wid / 5 - 8, x, y + 4 * wid / 5 - 8);

    outtextxy(x + len - 15, y + wid / 5, "Q");
    line(x + len, y + wid / 4 , x + len + wire, y + wid / 4);

    outtextxy(x + len - 15, y + 3 * wid / 5 + 10, "Q'");
    line(x + len, y + 4 * wid / 5 - 8, x + len + 25, y + 4 * wid / 5 - 8);
}

void andGate(int x, int y)
{
    int length = 15;
    line(x, y - 10, x, y + 10);
    line(x, y - 10, x + length, y - 10);
    line(x, y + 10, x + length, y + 10);
    arc(x + length, y, -90, 90, 10);
}

void drawClock(int x, int y, int len, int wid, int wire)
{
    line(x - 15, y + wid / 2, x, y + wid / 2);
    line(x - 15, y + wid / 2, x - 15, y + wid + 25);

    line(x, y + wid / 2 + 5, x + 10, y + wid / 2);
    line(x, y + wid / 2 - 5, x + 10, y + wid / 2);
}

int SA(int* bin)
{
    return bin[0] * bin[1] * bin[2];
}

int RA(int* bin)
{
    return bin[0] * bin[3];
}

int SB(int* bin)
{
    return (1 - bin[2]) * bin[1] * bin[0];
}

int RB(int* bin)
{
    return bin[0] * bin[1] * bin[2];
}

int SC(int* bin)
{
    return bin[0] * (1 - bin[1]) * (1 - bin[3]);
}

int RC(int* bin)
{
    return bin[1] * bin[0];
}

int SD(int* bin)
{
    return 1 - bin[0];
}

int RD(int* bin)
{
    return bin[0];
}

int ffOut(int s, int r, int prev)
{
    if (s == 0 && r == 0)
    {
        return prev;
    }
    else if (s == 0 && r == 1)
    {
        return 0;
    }
    else if (s == 1 && r == 0)
    {
        return 1;
    }
}

void drawPulse(int a, int b, int* bin)
{
    int prev[4];
    prev[0] = bin[0];
    prev[1] = bin[1];
    prev[2] = bin[2];
    prev[3] = bin[3];

    bin[0] = ffOut(SD(prev), RD(prev), prev[0]);
    bin[1] = ffOut(SC(prev), RC(prev), prev[1]);
    bin[2] = ffOut(SB(prev), RB(prev), prev[2]);
    bin[3] = ffOut(SA(prev), RA(prev), prev[3]);

    int x, y;
    x = a;
    y = b;

    line(x, y, x, y - 30);
    line(x, y - 30, x + 30, y - 30);
    line(x + 30, y - 30, x + 30, y);
    line(x + 30, y, x + 60, y);

    cout << endl;
    for (int j = 0; j < 4; j++)
    {
        cout << bin[3 - j] << " " ;
    }
    cout << endl;
    if (bin[0] == 0)
    {
        y = b + 50;
        if (prev[0] == 1)
        {
            line(x, y - 30, x, y);
        }
        line(x, y, x + 60, y);
    }
    else
    {
        y = b + 50;
        if (prev[0] == 0)
        {
            line(x, y, x, y - 30);
        }
        line(x, y - 30, x + 60, y - 30);
    }

    if (bin[1] == 0)
    {
        y = b + 100;
        if (prev[1] == 1)
        {
            line(x, y, x, y - 30);
        }
        line(x, y, x + 60, y);
    }
    else
    {
        y = b + 100;
        if (prev[1] == 0)
        {
            line(x, y, x, y - 30);
        }
        line(x, y - 30, x + 60, y - 30);
    }

    if (bin[2] == 0)
    {
        y = b + 150;
        if (prev[2] == 1)
        {
            line(x, y, x, y - 30);
        }
        line(x, y, x + 60, y);
    }
    else
    {
        y = b + 150;
        if (prev[2] == 0)
        {
            line(x, y, x, y - 30);
        }
        line(x, y - 30, x + 60, y - 30);
    }

    if (bin[3] == 0)
    {
        y = b + 200;
        if (prev[3] == 1)
        {
            line(x, y, x, y - 30);
        }
        line(x, y, x + 60, y);
    }
    else
    {
        y = b + 200;
        if (prev[3] == 0)
        {
            line(x, y, x, y - 30);
        }
        line(x, y - 30, x + 60, y - 30);
    }
}

void drawCircuit(int* init)
{
    outtext("Graphics Program") ;

    setfillstyle(SOLID_FILL, WHITE);

    int a, b;
    a = 200;
    b = 150;

    int len, wid, wire, gap;
    len = 75;
    wid = 125;
    wire = 25;
    gap = 250;

    char name[100];
    name[1] = '\0';

    name[0] = 'A';
    drawFlipFlop(a, b, len, wid, wire, name);
    drawClock(a, b, len, wid, wire);
    line(a + len + wire, b + wid / 4, a + len + wire, b - wid / 4);
    andGate(a - wire - 25, b + wid / 4); // 25 is and gate length
    andGate(a - wire - 25, b + 4 * wid / 5 - 8);

    name[0] = 'B';
    drawFlipFlop(a + gap, b, len, wid, wire, name);
    drawClock(a + gap, b, len, wid, wire);
    line(a + gap + len + wire, b + wid / 4, a + gap + len + wire, b - 2 * wid / 4);
    andGate(a - wire - 25 + gap, b + wid / 4); // 25 is and gate length
    andGate(a - wire - 25 + gap, b + 4 * wid / 5 - 8);

    name[0] = 'C';
    drawFlipFlop(a + gap * 2, b, len, wid, wire, name);
    drawClock(a + gap * 2, b, len, wid, wire);
    line(a + gap * 2 + len + wire, b + wid / 4, a + gap * 2 + len + wire, b - 3 * wid / 4);
    andGate(a - wire - 25 + 2 * gap, b + wid / 4); // 25 is and gate length
    andGate(a - wire - 25 + 2 * gap, b + 4 * wid / 5 - 8);

    name[0] = 'D';
    drawFlipFlop(a + gap * 3, b, len, wid, wire, name);
    drawClock(a + gap * 3, b, len, wid, wire);
    line(a + gap * 3 + len + wire, b + wid / 4, a + gap * 3 + len + wire, b - wid);

    line(a - 75, b + wid + 25, a + gap * 3 - 15, b + wid + 25);
    outtextxy(a - 106, b + wid + 16, "CLK");
    rectangle(a - 110, b + wid + 12, a - 75, b + wid + 37);

    line(a + gap + len + wire, b - 2 * wid / 4, a - wire - 35, b - 2 * wid / 4);
    line(a - wire - 35, b - 2 * wid / 4, a - wire - 35, b + wid / 4 - 8);
    line(a - wire - 35, b + wid / 4 - 8, a - wire - 25, b + wid / 4 - 8);   // B

    line(a + gap * 2 + len + wire, b - 3 * wid / 4, a - wire - 45, b - 3 * wid / 4);
    line(a - wire - 45, b - 3 * wid / 4, a - wire - 45, b + wid / 4);
    line(a - wire - 45, b + wid / 4, a - wire - 25, b + wid / 4);   // C

    line(a + gap * 3 + len + wire, b - wid, a - wire - 55, b - wid);
    line(a - wire - 55, b - wid, a - wire - 55, b + 4 * wid / 5 - 4);
    line(a - wire - 55, b + wid / 4 + 8, a - wire - 25, b + wid / 4 + 8);   // D
    line(a - wire - 55, b + 4 * wid / 5 - 4, a - wire - 25, b + 4 * wid / 5 - 4);
    fillellipse(a - wire - 55, b + wid / 4 + 8, 4, 4);

    line(a + len + wire, b - wid / 4, a - wire - 65, b - wid / 4);
    line(a - wire - 65, b - wid / 4, a - wire - 65, b + 4 * wid / 5 - 12);
    line(a - wire - 65, b + 4 * wid / 5 - 12, a - wire - 25, b + 4 * wid / 5 - 12);   // A

    line(a + gap * 3 - wire - 20, b - wid, a + gap * 3 - wire - 20, b + 4 * wid / 5 - 8);
    line(a + gap * 3 - wire - 20, b + 4 * wid / 5 - 8, a + gap * 3 - wire, b + 4 * wid / 5 - 8);    // D
    fillellipse(a + gap * 3 - wire - 20, b - wid, 4, 4);

    line(a + gap * 3 + len + 25, b + 4 * wid / 5 - 8, a + gap * 3 + len + 25, b + wid + 50);    // D'
    line(a + gap * 3 + len + 25, b + wid + 50, a + gap * 3 - wire, b + wid + 50);
    line(a + gap * 3 - wire, b + wid + 50, a + gap * 3 - wire, b + wid / 4);

    line(a + gap - wire - 35, b - 2 * wid / 4, a + gap - wire - 35, b + 4 * wid / 5 - 16);
    line(a + gap - wire - 35, b + 4 * wid / 5 - 16, a + gap - wire - 25, b + 4 * wid / 5 - 16);   // B  for both s and r
    fillellipse(a + gap - wire - 35, b - 2 * wid / 4, 4, 4);

    line(a + gap - wire - 45, b - 3 * wid / 4, a + gap - wire - 45, b + 4 * wid / 5 - 8);   // C
    line(a + gap - wire - 45, b + 4 * wid / 5 - 8, a + gap - wire - 25, b + 4 * wid / 5 - 8);
    line(a + gap - wire - 45, b + wid / 4 - 8, a + gap - wire - 25, b + wid / 4 - 8);
    fillellipse(a + gap - wire - 45, b - 3 * wid / 4, 4, 4);
    fillellipse(a + gap - wire - 45, b + wid / 4 - 8, 4, 4);

    line(a + gap - wire - 55, b - wid, a + gap - wire - 55, b + 4 * wid / 5);
    line(a + gap - wire - 55, b + 4 * wid / 5, a + gap - wire - 25, b + 4 * wid / 5); // D
    line(a + gap - wire - 55, b + wid / 4, a + gap - wire - 25, b + wid / 4);
    fillellipse(a + gap - wire - 55, b - wid, 4, 4);
    fillellipse(a + gap - wire - 55, b + wid / 4, 4, 4);

    line(a + gap + len + 25, b + 4 * wid / 5 - 8,  a + gap + len + 25, b + wid + 50);   // B'
    line(a + gap + len + 25, b + wid + 50, a + gap - wire - 65, b + wid + 50);
    line(a + gap - wire - 65, b + wid + 50, a + gap - wire - 65, b + wid / 4 + 8);
    line(a + gap - wire - 65, b + wid / 4 + 8, a + gap - wire - 25, b + wid / 4 + 8);

    line(a + 2 * gap - wire - 35, b - 3 * wid / 4, a + 2 * gap - wire - 35, b + 4 * wid / 5 - 12);
    line(a + 2 * gap - wire - 35, b + 4 * wid / 5 - 12, a + 2 * gap - wire - 25, b + 4 * wid / 5 - 12);     // C
    fillellipse(a + 2 * gap - wire - 35, b - 3 * wid / 4, 4, 4);

    line(a + 2 * gap - wire - 45, b - wid, a + 2 * gap - wire - 45, b + 4 * wid / 5 - 4);
    line(a + 2 * gap - wire - 45, b + 4 * wid / 5 - 4, a + 2 * gap - wire - 25, b + 4 * wid / 5 - 4);      // D
    fillellipse(a + 2 * gap - wire - 45, b - wid, 4, 4);
    fillellipse(a + 2 * gap - wire - 45, b + wid / 4 - 8, 4, 4);

    line(a + 2 * gap - wire - 45, b + wid / 4 - 8, a + 2 * gap - wire - 25, b + wid / 4 - 8);   // D for s

    line(a + gap * 2 + len + 25, b + 4 * wid / 5 - 8, a + gap * 2 + len + 25, b + wid + 50);
    line(a + gap * 2 + len + 25, b + wid + 50, a + gap * 2 - wire - 55, b + wid + 50);
    line(a + gap * 2 - wire - 55, b + wid + 50, a + gap * 2 - wire - 55, b + wid / 4);
    line(a + gap * 2 - wire - 55, b + wid / 4, a + gap * 2 - wire - 25, b + wid / 4);       // C'

    line(a + len + 25, b + 4 * wid / 5 - 8, a + len + 25, b + wid + 75);
    line(a + len + 25, b + wid + 75, a + 2 * gap - wire - 65, b + wid + 75);
    line(a + 2 * gap - wire - 65, b + wid + 75, a + 2 * gap - wire - 65, b + wid / 4 + 8);
    line(a + 2 * gap - wire - 65, b + wid / 4 + 8, a + 2 * gap - wire - 25, b + wid / 4 + 8);

    int posx, posy;
    posx = 100;
    posy = 450;
    int i = init[0] + 2 * init[1] + 4 * init[2] + 8 * init[3];
    int bin[4];
    bin[3] = init[3];
    bin[2] = init[2];
    bin[1] = init[1];
    bin[0] = init[0];

    line(posx - 30, posy, posx, posy);
    outtextxy(30, 442, "CLK");

    if (bin[0] == 0)
    {
        line(posx - 30, posy + 50, posx, posy + 50);
    }
    else
    {
        line(posx - 30, posy + 20, posx, posy + 20);
    }
    outtextxy(40, 492, "D");

    if (bin[1] == 0)
    {
        line(posx - 30, posy + 100, posx, posy + 100);
    }
    else
    {
        line(posx - 30, posy + 70, posx, posy + 70);
    }
    outtextxy(40, 542, "C");

    if (bin[2] == 0)
    {
        line(posx - 30, posy + 150, posx, posy + 150);
    }
    else
    {
        line(posx - 30, posy + 120, posx, posy + 120);
    }
    outtextxy(40, 592, "B");

    if (bin[3] == 0)
    {
        line(posx - 30, posy + 200, posx, posy + 200);
    }
    else
    {
        line(posx - 30, posy + 170, posx, posy + 170);
    }
    outtextxy(40, 642, "A");

    rectangle(a - 110, b + wid + 52, a - 55, b + wid + 77);
    outtextxy(a - 105, b + wid + 55, "RESET");

    while(1)
    {
        int p = mousex();
        int q = mousey();
        if(ismouseclick(WM_LBUTTONUP) && (p >= a - 110 && p <= a - 55 && q >= b + wid + 12 && q <= b + wid + 37))
        {
            i = (i + 1) % 10;
            drawPulse(posx, posy, bin);
            posx += 60;
        }
        clearmouseclick(WM_LBUTTONUP);
        if (ismouseclick(WM_LBUTTONUP) && (p >= a - 110 && p <= a - 75 && q >= b + wid + 52 && q <= b + wid + 77))
        {
            cleardevice();
            int init[4] = {0};
            drawCircuit(init);
        }
        clearmouseclick(WM_LBUTTONUP);
    }
}

int main()
{
    int init[4] = {0};

    int gd = DETECT, gm, err ;
    initwindow(1920, 1080);
    err = graphresult() ;
    if(err != grOk)
    {
        cout << "Graphics Error", grapherrormsg(err) ;
        getch() ;
        exit(1) ;
    }

    drawCircuit(init);

    getch() ;
    closegraph() ;
    return 0;
}
