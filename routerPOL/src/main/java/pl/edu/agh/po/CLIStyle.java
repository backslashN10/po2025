package pl.edu.agh.po;

import static org.fusesource.jansi.Ansi.ansi;

public final class CLIStyle
{
    private CLIStyle() {}
    
    public static String green(String text) {
        return ansi().fgGreen().a(text).reset().toString();
    }
    public static String blue(String text)
    {
        return ansi().fgBlue().a(text).reset().toString();
    }
    public static String yellow(String text)
    {
        return ansi().fgYellow().a(text).reset().toString();
    }
    public static String red(String text)
    {
        return ansi().fgRed().a(text).reset().toString();
    }
}
