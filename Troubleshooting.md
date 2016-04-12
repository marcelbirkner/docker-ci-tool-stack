# FAQ

## Having problems downloading docker images?

**Error:** Network timed out while trying to connect to https://index.docker.io/

**Solution**

```
# Add nameserver to DNS (probably need to do "sudo su" first)
echo "nameserver 8.8.8.8" > /etc/resolv.conf

# Restart the environment
$ docker-machine restart default

# Refresh your environment settings
$ eval $(docker-machine env default)
```
I also needed to do this inside the docker-machine:
```
$ docker-machine ssh default
$ echo "nameserver 8.8.8.8" > /etc/resolv.conf
```

## No Internet Connection from Docker Container

```
# Login to Docker VM
$ docker-machine ssh default

# Run DHCP client
$ sudo udhcpc

# Restart docker process
$ sudo /etc/init.d/docker restart
```
