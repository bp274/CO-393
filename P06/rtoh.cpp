#include <iostream>
#include <string>
#include <utility>
#include <vector>
#include <math.h>
#include <graphics.h>

class TowerOfHanoi
{
    private:
        int disks;
        std::vector<std::pair<int, int> > towers[3];

    public:
        TowerOfHanoi(int noOfDisks)
        {
            disks = noOfDisks;

            int width = 220;
            for (int i = 0; i < disks; i++)
            {
                std::pair<int, int> p;
                p.first = width;
                p.second = i + 1;
                towers[0].push_back(p);
                width = width - 10;
            }


            int gd = DETECT, gm, err;
            initwindow(1920, 1080);
            err = graphresult();
            if (err != grOk)
            {
                std::cout << "Graphics Error", grapherrormsg(err);
                getch();
                exit(1);
            }

            display();
            moves('A', 'B', 'C', disks);
            display();


            getch();
            closegraph();

        }

        void moves(char A, char B, char C, int noOfDisks)
        {
            if (noOfDisks == 0)
            {
                return;
            }

            moves(A, B, C, noOfDisks - 1);

            towers[B - 65].push_back(towers[A - 65].back());
            towers[A - 65].pop_back();

            display();

            std::cout << "\nMove plate " << noOfDisks << " from " << A << " to " << B;
            moves(C, B, A, noOfDisks - 1);

            towers[C - 65].push_back(towers[B - 65].back());
            towers[B - 65].pop_back();

            display();
            std::cout << "\nMove plate " << noOfDisks << " from " << B << " to " << C;

            moves(A, B, C, noOfDisks - 1);
        }

        void displayDisks()
        {
            for (int i = 0; i < 3; i++)
            {
                int base = 450;
                std::vector<std::pair<int, int> >::iterator it;
                for (it = towers[i].begin(); it != towers[i].end(); it++)
                {
                    int width = (*it).first;
                    int color = (*it).second;

                    setfillstyle(SOLID_FILL, color);

                    bar(150 + i * 400 - width / 2, base - 20, 150 + i * 400 + width / 2, base);
                    base = base - 20;
                }
            }
        }

        void displayRods()
        {
            outtextxy(940, 50, "C");
            bar(940, 450, 960, 150);
            line(850, 450, 1050, 450);

            outtextxy(540, 50, "B");
            bar(540, 450, 560, 150);
            line(450, 450, 650, 450);

            outtextxy(140, 50, "A");
            bar(140, 450, 160, 150);
            line(50, 450, 250, 450);
        }

        void display()
        {
            cleardevice();

            setfillstyle(SOLID_FILL, WHITE);

            displayRods();
            displayDisks();

            delay(1);
        }
};

int main()
{
    int disks;
    std::cout << "Enter the number of disks :- ";
    std::cin >> disks;

    TowerOfHanoi toh(disks);

    std::cout << "\nNumber of moves :- " << pow(3, disks) - 1 << std::endl;
}
