FROM quay.io/devfile/universal-developer-image:ubi8-latest

USER root

# Tilt
RUN curl -L https://github.com/tilt-dev/tilt/releases/download/v0.33.9/tilt.0.33.9.linux.x86_64.tar.gz | tar -C /usr/local/bin -xz && \
    chmod +x /usr/local/bin/tilt
ENV TILT_HOST=0.0.0.0

# Angular Cli
RUN npm install -g @angular/cli \
    && chgrp -R 0 /home/user/.npm \
    && chmod -R g=u /home/user/.npm

USER 1001
