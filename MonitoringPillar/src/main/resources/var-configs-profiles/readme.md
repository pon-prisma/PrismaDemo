ENVIRONMENT CONFIGURATION PROFILES
===

This folders contain environment-specific configuration files.

This are automatically loaded at runtime, based on the server hostname.
The hostname MUST be set with the FQDN and must contain the name of a profile.
---
Example:
	INFN server:
		hostname: `xxx.**infn**.yyy`
		properties folder: `var-configs-profiles/**infn**`