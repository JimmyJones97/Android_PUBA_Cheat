package com.tsml.hkl.Utils;


import android.util.Log;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.tsml.hkl.enty.ProcessInfo;


public class ProcessManager {


    public static List<ProcessInfo> parseProcessList() {

        List<ProcessInfo> processInfos;

        Process process = null;
        BufferedReader successResult = null;
        processInfos = new ArrayList<>();
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            String cmd = "ps -A";
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
                    /*if (name.contains("/")) {
                        name = name.substring(name.lastIndexOf("/") + 1);
                    }*/
                ProcessInfo processInfo = new ProcessInfo();
                processInfo.setPackageName(packageName);
                processInfo.setPid(pid);
                processInfo.setName("test");
                processInfos.add(processInfo);

            }
            return processInfos;

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

           /* if (process != null) {
                process.destroy();
            }*/
        }
        return null;

    }


}
