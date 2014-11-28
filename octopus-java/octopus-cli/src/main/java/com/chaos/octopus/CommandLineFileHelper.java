package com.chaos.octopus;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.*;

public class CommandLineFileHelper
{
    private CommandLine cmd;
    private String filePath;

    public CommandLineFileHelper(CommandLine cmd) throws ParseException
    {
        this.cmd = cmd;

        filePath = getFilePath();
    }

    private String getFilePath() throws ParseException
    {
        if(!hasFile())
            throw new ParseException("File not specified");

        String filePath = cmd.getArgs()[cmd.getArgs().length - 1];

        if(!fileExists(filePath))
            throw new ParseException(String.format("File '%s' cannot be found", filePath));

        return filePath;
    }

    private boolean hasFile()
    {
        return cmd.getArgs().length > 0;
    }

    private static boolean fileExists(String filePath)
    {
        return new File(filePath).exists();
    }

    public String readFile() throws IOException
    {
        StringBuilder sb = new StringBuilder();

        try(BufferedReader reader = getStream())
        {
            String line;

            while((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        }

        return sb.toString();
    }

    public BufferedReader getStream() throws IOException
    {
        return new BufferedReader(new FileReader(filePath));
    }
}
