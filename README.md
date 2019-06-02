# i18nedit

i18nedit is a tool for editing json translation files in your git repo.

![screenshot](screenshot.png?raw=true)

## Getting started

### ...using Docker

first we need to create a config file with the following content.

```json5
{
  "gitProjectPath": "/i18nedit/project", // don't edit
  "sshKeyPath": "/i18nedit/id_rsa", // don't edit
  "relativeTranslationFilePath": "translations/", // path of your translations folder relative to the repo
  "gitUri": "git@git.example.com:user/repo.git" // your repo uri (currently just ssh)
}
```

then put in the absolute path to the config file and your ssh key and run the following command

```bash
sudo docker run -p 8080:8080 -v /path/to/id_rsa:/i18nedit/id_rsa -v /path/to/config.json:/i18nedit/config.json foolsparadise/i18nedit
```
