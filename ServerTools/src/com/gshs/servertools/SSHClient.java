package com.gshs.servertools;

import java.util.*;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public class SSHClient {
    int port;
    String username, host, password;
    Session session; Channel channel; ChannelExec channelExec; JSch jsch; ChannelSftp channelSftp;
    public SSHClient() { }
    public boolean sshAccess(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        jsch = new JSch();
        try {
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("SSH OK");
            return true;
        }
        catch(Exception e) {
            System.out.println("SSH Failed");
            return false;
        }
    }
    public String Exec(String cmd) throws Exception{
        
        channel = session.openChannel("exec");
        channelExec = (ChannelExec) channel;
        channelExec.setPty(true);
        channelExec.setPtyType("vt100", 100, 100, 100, 100);
        channelExec.setCommand(cmd);
      
        StringBuilder outputBuffer = new StringBuilder();
        InputStream in = channel.getInputStream();
        ((ChannelExec) channel).setErrStream(System.err);         
        channel.connect();   
        
        byte[] tmp = new byte[1024];
        String result = "";
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                outputBuffer.append(new String(tmp, 0, i));
                if (i < 0) break;
            }
            if (channel.isClosed()) {
                result += outputBuffer.toString();
                channel.disconnect();
                break;
            }
        }
        channelExec.disconnect();
        channel.disconnect();
        return result;
    }
    public void download(String sourcePath, String destinationPath) throws Exception{
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
        channelSftp.get(sourcePath, destinationPath); 
        channelSftp.disconnect();
        channel.disconnect();
        channel = session.openChannel("sftp get fin");
    }
    public void upload(String sourcePath, String destinationPath) throws Exception {
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
        channelSftp.put(sourcePath, destinationPath); 
        channelSftp.disconnect();
        channel.disconnect();
        channel = session.openChannel("sftp put fin");
    }
    public void disconnect()
    {
        channel.disconnect();
        session.disconnect();
    }
}
