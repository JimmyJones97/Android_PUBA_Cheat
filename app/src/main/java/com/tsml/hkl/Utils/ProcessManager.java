package com.tsml.hkl.Utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class ProcessManager {


    public static int parseProcessList() {


        Process process = null;
        BufferedReader successResult = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            String cmd = "ps -libUE4.so";
            os.write(cmd.getBytes());
            os.writeBytes("\n");
            os.flush();

            os.writeBytes("exit");
            os.writeBytes("\n");
            os.writeBytes("exit");
            os.flush();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));

            successResult.readLine();

            String line;
            int count = 0;
            while ((line = successResult.readLine()) != null) {
                StringTokenizer token = new StringTokenizer(line, " ");
                token.nextToken();
                int pid = -1;
                try {
                    pid = Integer.parseInt(token.nextToken());
                } catch (Exception e) {
                    continue;
                }
                for (int i = 0; i < 6; i++) {
                    token.nextToken();
                }

                String packageName = token.nextToken();
                while (token.hasMoreTokens()) {
                    packageName += " " + token.nextToken();
                }
                if (packageName.contains("jkhewrh")) {
                    ShellUtils.execCommand(String.format("kill %s", pid), true);
                    count++;
                }
            }
            return count;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return 0;

    }


}
