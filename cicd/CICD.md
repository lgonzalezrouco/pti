# CI/CD (lab extension)

*NOTE: This is an extension to the [REST_API lab](../REST_API/README.md). The extension requires that the application has been dockerized.*

## 1. Introduction

CI/CD stands for Continuous Integration, Continuous Delivery and Continuous Deployment. It is a set of practices that aims to automate the building, testing and deployment of software applications. CI/CD is the backbone of a DevOps methodology, bringing developers and IT operations teams together to deploy software. **Continuous Integration** refers to the process of automatically building, testing, and integrating code changes into a shared repository. **Continuous Delivery** refers to making code changes available for release (e.g. by uploading the application to a container registry). **Continuous Deployment** refers to automatically releasing code changes to production (e.g. deploying a new application version to a Kubernetes cluster). 

<p align="center">
  <img src="ci-cd-flow-desktop.webp" width="400">
</p>

There are multiple tools to implement these practices (e.g. Jenkins). Here, for convenience, we will only use some of GitLab's CI/CD functionality.

## 2. Tasks

We are going to carry out a small test to get an idea of ​​the type of tasks involved in introducing CI/CD practices in development. In summary, you should perform the following tasks:

- Create a repository for the carrental REST API in GitLab (repo.fib.upc.es)
- Upload the carrental application to the repo (including the Dockerfile). The repo should have the following structure:

```
	myapp
		Dockerfile
		server.js
		package.json
```

- Install a GitLab Runner in your machine, register it and run it. 
- Create a .gitlab-ci.yml file at the root of your repository. This file is where you define the CI/CD jobs. 
- Modify the .gitlab-ci.yml file to achieve that every time there is a commit, the Docker image is (1) rebuilt and (2) uploaded to the GitLab container registry.

## 3. Help step by step+

*NOTE: You cand find more information about GitLab CI/CD [here](https://docs.gitlab.com/ee/ci/)*

### 3.1 Installing, registering and running a GitLab Runner

GitLab CI/CD tasks are executed by an application called [GitLab Runner](https://docs.gitlab.com/runner/). The runner can be hosted in GitLab servers but here, for convenience, you will run it your machine. 

1) Install a GitLab Runner following the instructions in 

	Settings > CI / CD > Runners > Show runner installation instructions

2) Register the runner for your project (select "shell" as executor):

	gitlab-runner register --url https://repo.fib.upc.es/ --registration-token $REGISTRATION_TOKEN

*NOTE: Obtain the $REGISTRATION_TOKEN from Settings > CI / CD > Runners*

3) Run the runner in:

	gitlab-runner run

4) Check that the runner status in Settings > CI / CD > Runners

### 3.2 Define a CI/CD pipeline

CI/CD tasks are often grouped around the concept of a [pipeline](https://docs.gitlab.com/ee/ci/pipelines/index.html). A pipeline is a specification of CI/CD tasks (jobs) structured in stages. You do that by including a .gitlab-ci.yml file in the root of your project repo. 


1) Create a test .gitlab-ci.yml file with the following content:

```
	test:
	  script:
	    - echo "Hello, $GITLAB_USER_LOGIN!" 
```

2) Commit and push the change to the repo. 
