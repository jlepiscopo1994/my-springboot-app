# Monitoring and Alerting Documentation

## HTTP 5xx Error Rate Alert

- **Panel Name:** HTTP 5xx Error Rate (%)
- **Dashboard:** [Grafana Dashboard URL](http://localhost:3000/d/[your-dashboard-id])
- **Prometheus Query:**

(
sum(increase(http_server_requests_seconds_count{status=~"5.."}[1m]))
/
sum(increase(http_server_requests_seconds_count[1m]))
) * 100

- **Alert Condition:**  
- Triggers if HTTP 5xx error rate is above *[Your Threshold]%* for *[Pending Period, e.g. 1 minute]*.

- **How to Test the Alert:**  
- Send repeated requests to `/test-error` in your application to generate 500 errors.
- Watch the panel and alert status in Grafana under **Alerting → Alert rules**.
- The alert should move from "Normal" to "Pending" and (if you keep errors up) to "Firing".
- When errors stop, state returns to "Normal".

- **Notification Contact Point:**  
- [e.g. Email, Slack, Teams, etc.-describe or link to setup.]

- **Actions on Alert:**  
- [e.g. Investigate logs, check recent deployments, escalate if needed.]

---

## Other Monitoring Panels (Optional)

- **JVM Memory Usage**
- **HTTP Request Rate**
- **Custom Metrics (describe if any)**
