{{/*
Expand the name of the chart.
*/}}
{{- define "ai.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "ai.fullname" -}}
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
{{- define "ai.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "ai.labels" -}}
helm.sh/chart: {{ include "ai.chart" . }}
{{ include "ai.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "ai.selectorLabels" -}}
app.kubernetes.io/name: {{ include "ai.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use for AI services
*/}}
{{- define "ai.serviceAccountName" -}}
{{- $root := . -}}
{{- $aiType := .Values.aiType | default "ollama" -}}
{{- $aiTypeArg := .aiType -}}

{{/* Use aiType from argument if provided, otherwise use the global aiType value */}}
{{- if $aiTypeArg -}}
  {{- $aiType = $aiTypeArg -}}
{{- end -}}

{{/* Check the specific AI type's serviceAccount configuration */}}
{{- if (index $root.Values $aiType).serviceAccount.create -}}
  {{- $fullName := include "ai.fullname" $root -}}
  {{- default $fullName (index $root.Values $aiType).serviceAccount.name -}}
{{- else -}}
  {{- default "default" (index $root.Values $aiType).serviceAccount.name -}}
{{- end -}}
{{- end }}

{{/*
Create the name of the service account to use for OpenWeb UI
*/}}
{{- define "ai.openwebServiceAccountName" -}}
{{- if .Values.openweb.serviceAccount.create }}
{{- default (include "ai.fullname" .) .Values.openweb.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.openweb.serviceAccount.name }}
{{- end }}
{{- end }}
