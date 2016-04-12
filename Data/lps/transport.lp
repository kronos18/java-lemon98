\ENCODING=ISO-8859-1
\Problem name: Transport

Minimize
 obj: 4 X1a + X1b + 2 X2a + 3 X2b + 130000 Y1a + 90000 Y1b + 80000 Y2a
      + 140000 Y2b + 150000 Yba
Subject To
 X1a_le_70000.Y1a#0:          X1a - 70000 Y1a <= 0
 X2a_le_30000.Y2a#1:          X2a - 30000 Y2a <= 0
 X1b_le_40000.Y1b#2:          X1b - 40000 Y1b <= 0
 X2b_le_80000.Y2b#3:          X2b - 80000 Y2b <= 0
 Xba_le_100000.Yba#4:         - 100000 Yba + Xba <= 0
 X1a_p_X1b_e_75000#5:         X1a + X1b  = 75000
 X2a_p_X2b_e_75000#6:         X2a + X2b  = 75000
 X1a_p_X2a_p_Xba_le_150000#7: X1a + X2a + Xba <= 150000
 X1b_p_X2b___Xba_le_50000#8:  X1b + X2b - Xba <= 50000
Bounds
 0 <= Y1a <= 1
 0 <= Y1b <= 1
 0 <= Y2a <= 1
 0 <= Y2b <= 1
 0 <= Yba <= 1
Generals
 Y1a  Y1b  Y2a  Y2b  Yba 
End
