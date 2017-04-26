IntegrationServer {
	defaults =
			[version                : "9.12",
			 installDeployerResource: 'false',
			 test                   : 'false',
			 executeACL             : 'Administrators',
			 useSSL                 : 'false']
}

ProcessModel {
	defaults =
			[version: '9.12',
			 test   : 'true',
			 useSSL : 'false']
}

MyWebmethodsServer {
	defaults =
			[
					version                          : '9.12',
					excludeCoreTaskEngineDependencies: 'true',
					cacheTimeOut                     : 900,
					includeSecurityDependencies      : 'true',
					rootFolderAliases                : 'folder.public',
					maximumFolderObjectCount         : 10000,
					enableAddtionalLogging           : 'true',
					maxFolderDepth                   : 25,
					test                             : 'true',
					useSSL                           : 'false'
			]
}


environments {
	DEV {
		IntegrationServers {
			is_node1 {
				host = "localhost"
				port = "8094"
				username = "Administrator"
				pwd = "manage"
			}
		}
	}
	TEST {
        IntegrationServers {
            is_node1 {
                host = "localhost"
                port = "8094"
                username = "Administrator"
                pwd = "manage"
            }
        }
    }
	QA {
		IntegrationServers {
			is_node1 {
				host = "localhost"
				port = "8094"
				username = "Administrator"
                //pwd = "manage"
				pwdHandle = "ADMIN_IS_QA"
			}
			is_node2 {
				host = "localhost"
				port = "8094"
				username = "Administrator"
                //pwd = "manage"
				pwdHandle = "ADMIN_IS_QA"
			}
		}
	}
	PRE_PROD_WITH_MWS_AND_BPM {
		IntegrationServers {
			is_node1 {
				host = "localhost"
				port = "8080"
				username = "Administrator"
				pwd = "manage"
			}
			is_node2 {
				host = "localhost"
				port = "8080"
				username = "Administrator"
				pwd = "manage"
			}
		}
		ProcessModels {
			bpm_node1 {
				host = "localhost"
				port = "5555"
				username = "Administrator"
				pwd = "manage"
			}
			bpm_node2 {
				host = "localhost"
				port = "5555"
				username = "Administrator"
				pwd = "manage"
			}
		}
		MWS {
			mws_node1 {
				host = "localhost"
				port = "5555"
				username = "Administrator"
				pwd = "manage"
			}
		}
	}
	PROD {
		IntegrationServers {
			is_node1 {
				host = "localhost"
				port = "8080"
				username = "Administrator"
				useSSL = "true"
			}
			is_node2 {
				host = "localhost"
				port = "8080"
				username = "Administrator"
				pwd = "manage"
				useSSL = "true"
			}
		}
	}
}
