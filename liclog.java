static String getLogFlexLM(String server, int port) throws IOException
    {
        try
        {
            String line = "\"C:\\flexnet_11_16_2_win64\\lmutil.exe\" lmstat -a  -c " + port + "@" + server;
            org.apache.commons.exec.CommandLine cmdLine = org.apache.commons.exec.CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog watchdog = new ExecuteWatchdog(80000);
            executor.setWatchdog(watchdog);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            int exitValue = executor.execute(cmdLine);
            return outputStream.toString();
        }
        catch (IOException e)
        {
            throw e;
        }
    }
    // ------------------------------------------------------------------------------------------------
    static LicFeature getLicFlexLM(String logStr, String featureName, String optFilename)
    {
        LicFeature lic = new LicFeature();
        lic.featureName = featureName;

        ArrayList<LicGroup> groups = getGroups(optFilename);

        if (logStr.length() > 10)
        {
            Scanner s = new Scanner(logStr).useDelimiter("Users of " + featureName + ":");
            if (s.hasNext())
            {
                s.next();
                if (s.hasNext())
                {
                    s = new Scanner(s.next()).useDelimiter("Users of ");
                    if (s.hasNext())
                    {
                        String lines[] = s.next().split("\n");
                        Pattern p = Pattern.compile("Total of (\\d*) (licenses|license) issued;  Total of (\\d*) license");
                        for (String line : lines)
                        {
                            Matcher m = p.matcher(line);
                            if (m.find() && (m.groupCount() == 3))
                            {
                                Integer valTotal = tryParse(m.group(1));
                                if (valTotal < 0 )
                                {
                                    lic.error = true;
                                    lic.errorMsg = "Invalid LOG response (total)";
                                }
                                lic.nLicsTotal = valTotal;
                            }

                        }
                        String elems[];
                        lic.nLicsIssued = 0;
                        for (String line : lines)
                        {
                            elems = line.trim().split(" ");
                            if (elems.length > 4)
                            {
                                if ((elems[3].substring(0,1).compareTo("(") == 0) &&
                                        (elems[3].substring(elems[3].length()-1,elems[3].length()).compareTo(")") == 0)
                                )
                                {
                                    HashMap<String, String> map = new HashMap();
                                    map.put("user", elems[0]);
                                    map.put("machine", elems[1]  + matchGroup(groups, elems[1]));
                                    lic.usersList.add(map);
                                    lic.nLicsIssued++;
                                }
                            }
                        }
                    }
                    else
                    {
                        lic.error = true;
                        lic.errorMsg = "Invalid LOG response (users)";
                    }
                }
            }
        }
        else
        {
            lic.error = true;
            lic.errorMsg = "Invalid FLEXLM response";
        }

        if ((lic.nLicsTotal == null) || (lic.nLicsIssued == null))
        {
            lic.error = true;
            lic.errorMsg = "Invalid LOG response";
        }

        return lic;
    }
