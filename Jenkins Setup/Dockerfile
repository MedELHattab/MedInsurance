FROM jenkins/jenkins:lts

USER root

# Install Docker client
RUN apt-get update && \
    apt-get -y install apt-transport-https ca-certificates curl gnupg2 software-properties-common && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && \
    apt-get update && \
    apt-get -y install docker-ce-cli

# Install Maven
RUN apt-get install -y maven

# Install SonarQube Scanner CLI
RUN curl -L https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.8.0.2856-linux.zip -o /tmp/sonar-scanner-cli.zip && \
    unzip /tmp/sonar-scanner-cli.zip -d /opt && \
    rm /tmp/sonar-scanner-cli.zip && \
    ln -s /opt/sonar-scanner-4.8.0.2856-linux/bin/sonar-scanner /usr/local/bin/sonar-scanner && \
    chmod +x /usr/local/bin/sonar-scanner

# Add Jenkins user to Docker group
RUN usermod -aG docker jenkins

# Switch back to jenkins user
USER jenkins

# Custom Jenkins port
ENV JENKINS_OPTS="--httpPort=8081"

# Expose port 8081
EXPOSE 8081