package com.mw.job.scheduler;

import com.liferay.dispatch.executor.BaseDispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutorOutput;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"dispatch.task.executor.name=simple-dispatch-task-executer", "dispatch.task.executor.type=simple-dispatch-task-executer"
	},
	service = DispatchTaskExecutor.class
)
public class SimpleDispatchTaskExecutor extends BaseDispatchTaskExecutor {
	private interface JOB_PROPERTIES {
		static final String TEST_PROPERTY_1 = "test.property_1";
		static final String TEST_PROPERTY_2 = "test.property_2";
		static final String USER_ID = "user.id";
	}
	
	@Override
	public String getName() {
		return "simple-dispatch-task-executer";
	}	
	
	@Activate
	protected void activate(Map<String, Object> properties)  throws Exception {
		_log.info("activate");
	}

	@Override
	public void doExecute(
			DispatchTrigger dispatchTrigger,
			DispatchTaskExecutorOutput dispatchTaskExecutorOutput)
		throws IOException, PortalException {

		_log.info("doExecute starting");
		
		UnicodeProperties properties = dispatchTrigger.getDispatchTaskSettingsUnicodeProperties();		
		
		// Reading known properties from the Job Details screen properties.
		String testProperty1 = properties.getProperty(JOB_PROPERTIES.TEST_PROPERTY_1, null);
		String testProperty2 = properties.getProperty(JOB_PROPERTIES.TEST_PROPERTY_2, null);
		String userIdString = properties.getProperty(JOB_PROPERTIES.USER_ID, null);

		
		_log.info("doExecute " + JOB_PROPERTIES.TEST_PROPERTY_1 + ": " + testProperty1);
		_log.info("doExecute " + JOB_PROPERTIES.TEST_PROPERTY_2 + ": " + testProperty2);
		_log.info("doExecute " + JOB_PROPERTIES.USER_ID + ": " + userIdString);


		Set<Entry<String, String>> entries = properties.entrySet();
		
		_log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// Log all of the properties...
        for (Map.Entry<String, String> entry : entries) {
        	_log.info(entry.getKey() + ": " + entry.getValue());
        }
        _log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
        if (Validator.isNull(testProperty1)) {
			_log.info("doExecute " + JOB_PROPERTIES.TEST_PROPERTY_1 + " property is null...");
			
			throw new PortalException("doExecute " + JOB_PROPERTIES.TEST_PROPERTY_1 + " property is null...");
		}
        
        if (Validator.isNull(testProperty1)) {
			_log.info("doExecute " + JOB_PROPERTIES.TEST_PROPERTY_2 + " property is null...");
			
			throw new PortalException("doExecute " + JOB_PROPERTIES.TEST_PROPERTY_2 + " property is null...");
		}
        
        User configUser = null;
        
        if (Validator.isNull(userIdString) || !isValidLong(userIdString)) {
			_log.info("doExecute " + JOB_PROPERTIES.USER_ID + " property is null or invalid...");
			
			throw new PortalException("doExecute " + JOB_PROPERTIES.USER_ID + " property is null or invalid...");
		} else {
			configUser = userLocalService.fetchUser(Long.parseLong(userIdString));
			
			if (configUser == null) {			
				_log.info("doExecute " + JOB_PROPERTIES.USER_ID + " user not found...");
				
				throw new PortalException("doExecute " + JOB_PROPERTIES.USER_ID + " user not found...");
			}
		}
        
        List<User> users = userLocalService.getCompanyUsers(dispatchTrigger.getCompanyId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS);
                
        for (User user: users) {
        	_log.info(user.getFullName());
        }
		
		_log.info("doExecute completed...");
	}
	
	private boolean isValidLong(String longString) {
		if (Validator.isName(longString)) return false;
		
	    try {
	        Long.parseLong(longString.trim());
	        
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}	
	
	@Reference(unbind = "-")
	private UserLocalService userLocalService;

	private static final Log _log = LogFactoryUtil.getLog(SimpleDispatchTaskExecutor.class);
}