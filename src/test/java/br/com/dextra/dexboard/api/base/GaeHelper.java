package br.com.dextra.dexboard.api.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

class GaeHelper {

	private static final Logger LOG = LoggerFactory.getLogger(GaeHelper.class);

	protected LocalServiceTestHelper helper;

	private LocalTaskQueueTestConfig task;

	private LocalDatastoreServiceTestConfig ds;

	public void prepareLocalServiceTestHelper() throws Exception {
		ds = new LocalDatastoreServiceTestConfig();
		ds.setDefaultHighRepJobPolicyUnappliedJobPercentage(0);
		List<LocalServiceTestConfig> list = new ArrayList<LocalServiceTestConfig>();
		list.add(ds);
		if (task != null) {
			list.add(task);
		}
		helper = new LocalServiceTestHelper(list.toArray(new LocalServiceTestConfig[0]));
		Map<String, Object> envs = new HashMap<String, Object>();
		envs.put("com.google.appengine.api.users.UserService.user_id_key", "10");
		helper.setEnvAttributes(envs);
		helper.setEnvIsLoggedIn(true);
		helper.setEnvIsAdmin(false);
		helper.setEnvEmail("test@dextra-sw.com");
		helper.setEnvAuthDomain("dextra-sw.com");
	}

	public void prepareLocalTaskQueue() {
		task = new LocalTaskQueueTestConfig();
		task.setDisableAutoTaskExecution(false);
		task.setCallbackClass(TaskCallback.class);
		task.setQueueXmlPath("src/main/webapp/WEB-INF/queue.xml");
	}

	public LocalServiceTestHelper getHelper() {
		return helper;
	}

	public LocalTaskQueueTestConfig getTask() {
		return task;
	}

	public LocalDatastoreServiceTestConfig getDs() {
		return ds;
	}

	public void bootLocalServiceTestHelper() throws Exception {
		helper.setUp();
	}

	public void shutdownLocalServiceTestHelper() {
		try {
			if (helper != null) {
				helper.tearDown();
			}
		} catch (Exception e) {
			LOG.info("error", e);
		}
	}

}
