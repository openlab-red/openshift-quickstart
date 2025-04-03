{{/*
Expand the name of the chart.
*/}}
{{- define "pipeline.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "pipeline.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "pipeline.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "pipeline.labels" -}}
helm.sh/chart: {{ include "pipeline.chart" . }}
{{ include "pipeline.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "pipeline.selectorLabels" -}}
app.kubernetes.io/name: {{ include "pipeline.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "pipeline.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "pipeline.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}


{{- define "pipeline.java-build" -}}
    - name: build-app
      params:
        - name: MAVEN_IMAGE
          value: >-
            image-registry.openshift-image-registry.svc:5000/openshift/{{ .Release.Name  }}:latest
        - name: GOALS
          value:
            - package
        - name: SUBDIRECTORY
          value: $(params.CONTEXT_DIR)
      runAfter:
        - fetch-repository
      taskRef:
        resolver: cluster
        params:
        - name: kind
          value: task
        - name: name
          value: maven
        - name: namespace
          value: openshift-pipelines
      workspaces:
        - name: source
          workspace: {{ include "pipeline.fullname" . }}-ws
        - name: maven_settings
          workspace: {{ include "pipeline.fullname" . }}-ws
{{- end }}

{{- define "pipeline.golang-build" -}}
    - name: build-app
      params:
        - name: CONTEXT
          value: $(params.CONTEXT_DIR)
      runAfter:
        - fetch-repository
      taskRef:
        resolver: hub
        params:
        - name: type
          value: tekton
        - name: kind
          value: task
        - name: name
          value: golang-build
        - name: version
          value: "0.3"
      workspaces:
        - name: source
          workspace: {{ include "pipeline.fullname" . }}-ws
{{- end }}