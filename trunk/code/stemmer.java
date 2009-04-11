// Decompiled by DJ v3.0.0.63 Copyright 2002 Atanas Neshkov  Date: 10/04/2009 11:23:54 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Stemmer.java

import java.io.*;

class Stemmer
{

    public Stemmer()
    {
        b = new char[50];
        i = 0;
        i_end = 0;
    }

    public void add(char ch)
    {
        if(i == b.length)
        {
            char new_b[] = new char[i + 50];
            for(int c = 0; c < i; c++)
                new_b[c] = b[c];

            b = new_b;
        }
        b[i++] = ch;
    }

    public void add(char w[], int wLen)
    {
        if(i + wLen >= b.length)
        {
            char new_b[] = new char[i + wLen + 50];
            for(int c = 0; c < i; c++)
                new_b[c] = b[c];

            b = new_b;
        }
        for(int c = 0; c < wLen; c++)
            b[i++] = w[c];

    }

    public String toString()
    {
        return new String(b, 0, i_end);
    }

    public int getResultLength()
    {
        return i_end;
    }

    public char[] getResultBuffer()
    {
        return b;
    }

    private final boolean cons(int i)
    {
        switch(b[i])
        {
        case 97: // 'a'
        case 101: // 'e'
        case 105: // 'i'
        case 111: // 'o'
        case 117: // 'u'
            return false;

        case 121: // 'y'
            return i != 0 ? !cons(i - 1) : true;
        }
        return true;
    }

    private final int m()
    {
        int n = 0;
        int i = 0;
        do
        {
            if(i > j)
                return n;
            if(!cons(i))
                break;
            i++;
        } while(true);
        i++;
        do
        {
            if(i > j)
                return n;
            if(!cons(i))
            {
                i++;
            } else
            {
                i++;
                n++;
                do
                {
                    if(i > j)
                        return n;
                    if(!cons(i))
                        break;
                    i++;
                } while(true);
                i++;
            }
        } while(true);
    }

    private final boolean vowelinstem()
    {
        for(int i = 0; i <= j; i++)
            if(!cons(i))
                return true;

        return false;
    }

    private final boolean doublec(int j)
    {
        if(j < 1)
            return false;
        if(b[j] != b[j - 1])
            return false;
        else
            return cons(j);
    }

    private final boolean cvc(int i)
    {
        if(i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2))
            return false;
        int ch = b[i];
        return ch != 119 && ch != 120 && ch != 121;
    }

    private final boolean ends(String s)
    {
        int l = s.length();
        int o = (k - l) + 1;
        if(o < 0)
            return false;
        for(int i = 0; i < l; i++)
            if(b[o + i] != s.charAt(i))
                return false;

        j = k - l;
        return true;
    }

    private final void setto(String s)
    {
        int l = s.length();
        int o = j + 1;
        for(int i = 0; i < l; i++)
            b[o + i] = s.charAt(i);

        k = j + l;
    }

    private final void r(String s)
    {
        if(m() > 0)
            setto(s);
    }

    private final void step1()
    {
        if(b[k] == 's')
            if(ends("sses"))
                k -= 2;
            else
            if(ends("ies"))
                setto("i");
            else
            if(b[k - 1] != 's')
                k--;
        if(ends("eed"))
        {
            if(m() > 0)
                k--;
        } else
        if((ends("ed") || ends("ing")) && vowelinstem())
        {
            k = j;
            if(ends("at"))
                setto("ate");
            else
            if(ends("bl"))
                setto("ble");
            else
            if(ends("iz"))
                setto("ize");
            else
            if(doublec(k))
            {
                k--;
                int ch = b[k];
                if(ch == 108 || ch == 115 || ch == 122)
                    k++;
            } else
            if(m() == 1 && cvc(k))
                setto("e");
        }
    }

    private final void step2()
    {
        if(ends("y") && vowelinstem())
            b[k] = 'i';
    }

    private final void step3()
    {
        if(k == 0)
            return;
        switch(b[k - 1])
        {
        case 98: // 'b'
        case 100: // 'd'
        case 102: // 'f'
        case 104: // 'h'
        case 105: // 'i'
        case 106: // 'j'
        case 107: // 'k'
        case 109: // 'm'
        case 110: // 'n'
        case 112: // 'p'
        case 113: // 'q'
        case 114: // 'r'
        default:
            break;

        case 97: // 'a'
            if(ends("ational"))
            {
                r("ate");
                break;
            }
            if(ends("tional"))
                r("tion");
            break;

        case 99: // 'c'
            if(ends("enci"))
            {
                r("ence");
                break;
            }
            if(ends("anci"))
                r("ance");
            break;

        case 101: // 'e'
            if(ends("izer"))
                r("ize");
            break;

        case 108: // 'l'
            if(ends("bli"))
            {
                r("ble");
                break;
            }
            if(ends("alli"))
            {
                r("al");
                break;
            }
            if(ends("entli"))
            {
                r("ent");
                break;
            }
            if(ends("eli"))
            {
                r("e");
                break;
            }
            if(ends("ousli"))
                r("ous");
            break;

        case 111: // 'o'
            if(ends("ization"))
            {
                r("ize");
                break;
            }
            if(ends("ation"))
            {
                r("ate");
                break;
            }
            if(ends("ator"))
                r("ate");
            break;

        case 115: // 's'
            if(ends("alism"))
            {
                r("al");
                break;
            }
            if(ends("iveness"))
            {
                r("ive");
                break;
            }
            if(ends("fulness"))
            {
                r("ful");
                break;
            }
            if(ends("ousness"))
                r("ous");
            break;

        case 116: // 't'
            if(ends("aliti"))
            {
                r("al");
                break;
            }
            if(ends("iviti"))
            {
                r("ive");
                break;
            }
            if(ends("biliti"))
                r("ble");
            break;

        case 103: // 'g'
            if(ends("logi"))
                r("log");
            break;
        }
    }

    private final void step4()
    {
        switch(b[k])
        {
        default:
            break;

        case 101: // 'e'
            if(ends("icate"))
            {
                r("ic");
                break;
            }
            if(ends("ative"))
            {
                r("");
                break;
            }
            if(ends("alize"))
                r("al");
            break;

        case 105: // 'i'
            if(ends("iciti"))
                r("ic");
            break;

        case 108: // 'l'
            if(ends("ical"))
            {
                r("ic");
                break;
            }
            if(ends("ful"))
                r("");
            break;

        case 115: // 's'
            if(ends("ness"))
                r("");
            break;
        }
    }

    private final void step5()
    {
        if(k == 0)
            return;
        switch(b[k - 1])
        {
        case 97: // 'a'
            if(!ends("al"))
                return;
            break;

        case 99: // 'c'
            if(!ends("ance") && !ends("ence"))
                return;
            break;

        case 101: // 'e'
            if(!ends("er"))
                return;
            break;

        case 105: // 'i'
            if(!ends("ic"))
                return;
            break;

        case 108: // 'l'
            if(!ends("able") && !ends("ible"))
                return;
            break;

        case 110: // 'n'
            if(!ends("ant") && !ends("ement") && !ends("ment") && !ends("ent"))
                return;
            break;

        case 111: // 'o'
            if((!ends("ion") || j < 0 || b[j] != 's' && b[j] != 't') && !ends("ou"))
                return;
            break;

        case 115: // 's'
            if(!ends("ism") || !ends("e"))
                return;
            break;

        case 116: // 't'
            if(!ends("ate") && !ends("iti"))
                return;
            break;

        case 117: // 'u'
            if(!ends("ous"))
                return;
            break;

        case 118: // 'v'
            if(!ends("ive"))
                return;
            break;

        case 122: // 'z'
            if(!ends("ize"))
                return;
            break;

        case 98: // 'b'
        case 100: // 'd'
        case 102: // 'f'
        case 103: // 'g'
        case 104: // 'h'
        case 106: // 'j'
        case 107: // 'k'
        case 109: // 'm'
        case 112: // 'p'
        case 113: // 'q'
        case 114: // 'r'
        case 119: // 'w'
        case 120: // 'x'
        case 121: // 'y'
        default:
            return;
        }
        if(m() > 1)
            k = j;
    }

    private final void step6()
    {
        j = k;
        if(b[k] == 'e')
        {
            int a = m();
            if(a > 1 || a == 1 && !cvc(k - 1))
                k--;
        }
        if(b[k] == 'l' && doublec(k) && m() > 1)
            k--;
    }

    public void stem()
    {
        k = i - 1;
        if(k > 1)
        {
            step1();
            step2();
            step3();
            step4();
            step5();
            step6();
        }
        i_end = k + 1;
        i = 0;
    }

    public static void main(String args[])
    {
        char w[] = new char[501];
        Stemmer s = new Stemmer();
        for(int i = 0; i < args.length; i++)
        {
            try
            {
                FileInputStream in = new FileInputStream(args[i]);
                try
                {
                    int k = 0;
                    do
                    {
                        int ch = in.read();
                        if(Character.isLetter((char)ch))
                        {
                            int j = 0;
                            do
                            {
                                ch = Character.toLowerCase((char)ch);
                                w[j] = (char)ch;
                                if(j < 500)
                                    j++;
                                ch = in.read();
                            } while(Character.isLetter((char)ch));
                            for(int c = 0; c < j; c++)
                                s.add(w[c]);

                            s.stem();
                            String u = s.toString();
                            System.out.println(u);
                        }
                        if(ch < 0)
                            break;
                        k++;
                    } while(true);
                    continue;
                }
                catch(IOException e)
                {
                    System.out.println((new StringBuilder("error reading ")).append(args[i]).toString());
                }
            }
            catch(FileNotFoundException e)
            {
                System.out.println((new StringBuilder("file ")).append(args[i]).append(" not found").toString());
            }
            break;
        }

    }

    private char b[];
    private int i;
    private int i_end;
    private int j;
    private int k;
    private static final int INC = 50;
}