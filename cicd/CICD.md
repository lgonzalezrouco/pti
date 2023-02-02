# CI/CD (lab extension)

*NOTE: This is an extension to the [REST_API lab](../REST_API/README.md). The extension requires that the application has been dockerized.*

## Introduction

CI/CD stands for Continuous Integration, Continuous Delivery and Continuous Deployment. It is a set of practices that aims to automate the building, testing and deployment of software applications. CI/CD is the backbone of a DevOps methodology, bringing developers and IT operations teams together to deploy software. **Continuous Integration** refers to the process of automatically building, testing, and integrating code changes into a shared repository. **Continuous Delivery** refers to making code changes available for release (e.g. by uploading the application to a container registry). **Continuous Deployment** refers to automatically releasing code changes to production (e.g. deploying a new application version to a Kubernetes cluster). 

<p align="center">
  <img src="ci-cd-flow-desktop.webp" width="600">
</p>

There are multiple tools to implement these practices (e.g. Jenkins). Here, for convenience, we will only use some of GitLab's CI/CD functionality.

## Lab extension

We are going to carry out a small test to get an idea of ​​the type of tasks involved in introducing CI/CD practices in development. In summary, you should perform the following tasks:

- Create a repository for the carrental REST API in GitLab (repo.fib.upc.es)
- Upload the carrental application to the repo (including the Dockerfile). The repo should have the following structure:

	myapp
		Dockerfile
		server.js
		package.json

- Install a GitLab Runner in your machine, and register it. 
- Create a .gitlab-ci.yml file at the root of your repository. This file is where you define the CI/CD jobs. 
- Modify the .gitlab-ci.yml file to para to achieve that every time there is a commit, the Docker image is (1) rebuilt and (2) uploaded to the GitLab container registry.



