
# i18nedit

  

i18nedit is a tool for editing json translation files in your git repo.

  

![screenshot](screenshot.png?raw=true)

  

## Getting started

  

### with a single command

  

```bash

sudo docker run -p 8080:8080 -v /path/to/id_rsa:/i18nedit/id_rsa -e GIT_URI=git@git.example.com:user/repo.git foolsparadise/i18nedit

```

  
  

| Environment Variables |  |
|--|--|
| `GIT_URI` |  ssh uri of your git repository |
| `GIT_BRANCH` | git branch |
| `RELATIVE_TRANSLATION_FILE_PATH` | the relative path in your repo where your translation files stay |
| `SSH_KEY_PATH` | path of the ssh key, must match the docker volume mapping |
| `GIT_PROJECT_PATH` | path where the repo is cloned (no need to modify this in docker) |


  

### or with Config File

  

create a config file with the following content.

  

```json5
{
	gitProjectPath: '/i18nedit/project', // don't edit
	sshKeyPath: '/i18nedit/id_rsa', // don't edit
	relativeTranslationFilePath: 'translations/', // path of your translations folder relative to the repo
	gitUri: 'git@git.example.com:user/repo.git', // your repo uri (currently just ssh)
	gitBranch: 'master'  // optional
}
```

  

then put in the absolute path to the config file and your ssh key and run the following command

  

```bash
sudo docker run -e CONFIG_FILE=/i18nedit/config.json -p 8080:8080 -v /path/to/id_rsa:/i18nedit/id_rsa -v /path/to/config.json:/i18nedit/config.json foolsparadise/i18nedit
```

