# Compatibility-breaking extraction of jira_id from description
## State of the Case
1. Separate description into taskId and description by business demand, prohibiting inclusion of taskId into the
description part.
2. Make the migration as seamless as possible while considering the system is running in production mode with its
current state. It is not possible to remove specific API as its being consumed by Uber Popug co., only deprecate.
3. The change is confirmed to be a breaking change, requiring.

## Solution
1. Design updated contract of POST /tasks and a set of v2 CUD and business events.
2. Upload updated contracts into a schema-registry as v2 for existing contracts.
3. Support CUD v2 events on employee-billing side:
   1. Adopt DB schema by adding a new nullable database field jira_id to the employee_billing.tasks table. jira_id is
   intentionally left nullable to adopt the system with existing payload in mind. Lack of jira_id means old format,
   while its presence is mandatory for all incoming events (including v1 starting with next release) after the feature
   related to this ADR is implemented.
   2. Ensure all the listeners of V1 events refrain from applying of structure validation due to the contract not being
   strictly enforced previously while trying to separate jira_id from description where it's possible.
   3. Pull the necessary contracts, provide their handling by the listeners. Ensure description of v2 events never
   has jira id included as a part of description.
4. Ensure events V1 are never produced by task-tracker and gradually migrate from them:
   1. Like in employee-billing, adopt DB schema by adding a new nullable database field jira_id to the
   task_tracker.tasks table. jira_id is intentionally left nullable to adopt the system with existing payload in mind.
   Lack of jira_id means old format, while its presence is mandatory for all incoming events (including v1 starting
   with next release) after the feature related to this ADR is implemented.
   2. Mark POST /tasks as deprecated:
      1. Add an optional jira_id field to the old contract as a backwards-compatible way to supply the id for existing
      consumers.
      2. Design a backwards incompatible contract of POST /v2/tasks with jira_id being mandatory for all of its users.
   3. Try supporting new format when possible with POST /tasks. Send v2 events when:
      1. jira_id is present as its optional body payload (remove the part from description if provided as well).
      2. jira_id is absent but description contains it exactly as the first part of payload in the form of:
      **\[jira-id\] \<Description\>**.
   4. Keep sending v1 events for POST /tasks when none of the conditions above are satisfied.
5. Implement POST /v2/tasks with breaking changes, strictly requiring the presence of jira_id and validation of
description.
6. Remove POST /tasks and all traces of it producing v1 events.
7. Run monitoring of employee-billing to ensure v1 events are never consumed.
8. Disable and remove listeners of v1 events.
9. Run database migrations and set jira_id to NOT NULL:
   1. Ensure task-tracker has jira_id for every single task, add generated IDs where necessary.
   2. Similar to task-tracker - ensure has jira_id for every single task by running a specific migration
   from task-tracker side: send TaskV2ConsistencyFixEvent to ensure appropriate replication.
   3. Run check on task-tracker: if no NULL values are present, add the NOT NULL constraint.
   4. Run checks on employee-billing: if no NULL values are present and all v1 events are handled, add the
   NOT NULL constraint.