FROM jenkins/jenkins:lts

USER root

# Install Docker client
RUN apt-get update && \
    apt-get -y install apt-transport-https ca-certificates curl gnupg lsb-release && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null && \
    apt-get update && \
    apt-get -y install docker-ce-cli

# Install Maven
RUN apt-get install -y maven

# Install SonarQube Scanner CLI
RUN curl -L https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.8.0.2856-linux.zip -o /tmp/sonar-scanner-cli.zip && \
    apt-get -y install unzip && \
    unzip /tmp/sonar-scanner-cli.zip -d /opt && \
    rm /tmp/sonar-scanner-cli.zip && \
    ln -s /opt/sonar-scanner-4.8.0.2856-linux/bin/sonar-scanner /usr/local/bin/sonar-scanner && \
    chmod +x /usr/local/bin/sonar-scanner

# Add Jenkins user to Docker group
RUN groupadd -f docker && usermod -aG docker jenkins

# Switch back to jenkins user
USER jenkins

# Custom Jenkins port
ENV JENKINS_OPTS="--httpPort=8081"

# Expose port 8081
EXPOSE 8081