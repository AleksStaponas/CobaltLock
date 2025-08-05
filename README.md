## Legal Disclaimer

This software is provided **strictly for educational, research, and ethical practice purposes only**.

-  **Do NOT use this tool on any system you do not own or have explicit permission to test.**
-  **Unauthorized use of this software may be illegal and is strictly prohibited.**
-  **The author takes no responsibility for any damage, data loss, or legal consequences resulting from misuse.**

By using this software, you agree to use it **ethically, lawfully**, and in **controlled environments such as CTFs, virtual labs, or testing networks**.

This tool is intended to help raise awareness and understanding of security threats — **not to promote or enable criminal activity**.

## Safety Features

- **Safe Decrypt Option:** Instantly restores files to prevent damage during testing.  
- **Reverse Shell:** Runs via Windows CMD for controlled simulation and has a connector for testing.  
- **Limited Impact:** Only files in a safe test folder are affected.

## Windows10FakeUpdate  
A fake Windows update screen used as a distraction that encrypts discovered files and estimates a time so it is ready and not as alarming to users. Furthermore, this behaviour can often be related to other malware types, such as viruses, that use that time to spread through vulnerabilities such as zero-days, or even install rootkits onto a compromised machine.

<p align="center">
  <img src="images/Windows10FakeUpdate.JPG" alt="WindowsFakeUpdate" />
</p>

## InitialPhase  
Initial lock screen phase with timer and shell connection. This is demonstrated as it is often used in real-world TTPs (Tactics, Techniques, and Procedures), and is commonly shown in attackers' OPSEC operations, which they can use to further escalate privileges or obfuscate logs.

<p align="center">
  <img src="images/FileLocker_BluePhase.JPG" alt="FirstPhase" />
</p>

## SecondPhase  
Critical phase with timer and increased demands, file deletion if requirements are met with simulated payment code. If demands are not met, this ethical ransomware runs a wipedown where the user's encrypted files are deleted, simulating similar behaviours to the NotPetya ransomware that would delete a compromised machine's files. In this case, it is simulated and only targets files in a specific directory.  

<p align="center">
  <img src="images/FileLocker_RedPhase.JPG" alt="SecondPhase" />
</p>

# Current isues
The current file discovery process fails when the specified path includes a directory. Once it encounters a directory, it is unable to encrypt any files contained within its subdirectories.

## Current file discovery
<p align="center">
  <img src="images/Diagrams/CurrentFileDiscoverer.png" alt="CurrentFileDiscovery" />
</p>

## Updated file discovery

<p align="center">
  <img src="images/Diagrams/ImprovedFileDiscoverer.png" alt="UpdatedFileDiscovery" />
</p>
