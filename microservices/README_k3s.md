
#https://docs.k3s.io/quick-start


Execute the following command will create a single-node Kubernetes cluster:

	curl -sfL https://get.k3s.io | sh -

The command also installs kubectl and creates a kubeconfig file (/etc/rancher/k3s/k3s.yaml). 

To be able to run k3s commands without sudo do the following:


	mkdir -p .kube && sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config && sudo chown $USER ~/.kube/config && chmod 600 ~/.kube/config && export KUBECONFIG=~/.kube/config

Test:

	kubectl get node
	
Launch a Docker registry:	
	
	docker run -d -p 5000:5000 --restart=always --name registry registry:2

Tag and push the image:

	docker tag helloworld:1.0 localhost:5000/helloworld 
	docker push localhost:5000/helloworld

kubectl create deployment helloworld --image=localhost:5000/helloworld --port=8080 --replicas=2
	

