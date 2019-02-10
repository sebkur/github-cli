package de.topobyte.githubcli.git;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.util.FS;

import com.jcraft.jsch.IdentityRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.agentproxy.AgentProxyException;
import com.jcraft.jsch.agentproxy.Connector;
import com.jcraft.jsch.agentproxy.RemoteIdentityRepository;
import com.jcraft.jsch.agentproxy.USocketFactory;
import com.jcraft.jsch.agentproxy.connector.SSHAgentConnector;
import com.jcraft.jsch.agentproxy.usocket.JNAUSocketFactory;

/**
 * @author Sebastian Kuerten (sebastian@topobyte.de)
 */
public class SshUtil
{

	public static void enableLogging()
	{
		JSch.setLogger(new SystemOutLogger());
	}

	public static class SystemOutLogger implements com.jcraft.jsch.Logger
	{
		static Map<Integer, String> name = new HashMap<>();
		static {
			name.put(new Integer(DEBUG), "DEBUG: ");
			name.put(new Integer(INFO), "INFO: ");
			name.put(new Integer(WARN), "WARN: ");
			name.put(new Integer(ERROR), "ERROR: ");
			name.put(new Integer(FATAL), "FATAL: ");
		}

		@Override
		public boolean isEnabled(int level)
		{
			return true;
		}

		@Override
		public void log(int level, String message)
		{
			System.err.print(name.get(new Integer(level)));
			System.err.println(message);
		}
	}

	public static void initSshAgentUsage()
	{
		initSshAgentUsage(null);
	}

	public static void initSshAgentUsage(final String pathSocket)
	{
		JschConfigSessionFactory sessionFactory = new JschConfigSessionFactory() {

			@Override
			protected void configure(OpenSshConfig.Host host, Session session)
			{
				session.setConfig("StrictHostKeyChecking", "false");
			}

			@Override
			protected JSch createDefaultJSch(FS fs) throws JSchException
			{
				Connector connector = null;
				try {
					if (SSHAgentConnector.isConnectorAvailable()) {
						USocketFactory usf = new JNAUSocketFactory();
						if (pathSocket == null) {
							connector = new SSHAgentConnector(usf);
						} else {
							connector = new SSHAgentConnector(usf, pathSocket);
						}
					}
				} catch (AgentProxyException e) {
					System.out.println(e);
				}

				JSch jsch = super.createDefaultJSch(fs);
				if (connector != null) {
					JSch.setConfig("PreferredAuthentications", "publickey");
					IdentityRepository irepo = new RemoteIdentityRepository(
							connector);
					jsch.setIdentityRepository(irepo);
				}
				return jsch;
			}
		};

		SshSessionFactory.setInstance(sessionFactory);
	}

}
