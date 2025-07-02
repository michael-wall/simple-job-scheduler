## Introduction ##
- This is a sample OSGi module that contains a simple Job Scheduler DispatchTaskExecutor.
- It checks for 2 specific custom properties, lists all of the custom properties and lists all users in the current Virtual Instance.

## Environment ##
- The module was built and tested with Liferay DXP 7.4 U92 (Liferay Workspace gradle.properties > liferay.workspace.product = dxp-7.4-u92)
- JDK 11 is expected for compile time and runtime.

## Configuration ##
- Build and deploy the custom OSGi module.
- Go to Control Panel > Configuration > Job Scheduler
- Click 'New' and select simple-dispatch-task-executor from the dropdown.
- Populate a name e.g. 'Simple Job Scheduler' and add the following custom properties (one per line in the format key=value) in the text and 'Save'.
```
test.property_1=value 1
test.property_2=value 2
test.property_3=value 3
user.id=99999 [this should be a userId of an Active user in the Virtual Instance]
```
- Select the newly created Job from the Grid screen and switch to the 'Job Scheduler Trigger' tab.
- Set to Active, add a Cron expression for example to run it every 5 minutes use 0 */5 * ? * * and then 'Save'.
- Note the 'Next Run Date' of the Job and confirm it runs as expected.
  - Alternatively click 'Run Now', and check the logs.
- Check the Logs tab of the Job to see the Run History.
  - A PortalException is thrown to trigger a Job 'Fail' if either of the following custom properties is missing: test.property_1 or test.property_2.
  - A PortalException is thrown to trigger a Job 'Fail' if the following custom properties is missing or invalid: user.id.
 
## Custom Properties ##
- These sample custom properties are included as examples of how the properties can be injected into the Job for configuration, rather than hardcoding values in the Job code.
- For example the same Task Executor can be used by more than 1 Job Scheduler. The custom properties can instruct the different Job Schedulers to do different things based on the logic in the Task Executor.

## Notes ##
- This is a ‘proof of concept’ that is being provided ‘as is’ without any support coverage or warranty.

## Reference ##
- https://learn.liferay.com/w/dxp/liferay-development/core-frameworks/job-scheduler-framework/creating-a-new-job-scheduler-task-executor
- https://learn.liferay.com/w/dxp/liferay-development/core-frameworks/job-scheduler-framework/using-job-scheduler#adding-a-new-job-scheduler-task
