# -*- mode: Python -*-

image = 'quay.io/' + os.environ['WORKSPACE_NAMESPACE'] + '/openshift-quickstart'

local_resource(
  'java-compile',
  'mvn clean package -DskipTests',
  deps=['src', 'pom.xml'])

load('ext://podman', 'podman_build')

podman_build(
  image,
  '.',
  extra_flags=['-f', 'src/main/docker/Dockerfile.jvm'],
  live_update=[
    sync('target/quarkus-app/lib/','/deployments/lib/'),
    sync('target/quarkus-app/*.jar','/deployments/'),
    sync('target/quarkus-app/app/','/deployments/app/'),
    sync('target/quarkus-app/quarkus/','/deployments/quarkus/')
])


allow_k8s_contexts('logged-user')

k8s_yaml('./target/kubernetes/openshift.yml')

k8s_resource('openshift-quickstart', port_forwards=[8080,5005])
