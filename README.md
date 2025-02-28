# Operating System Concepts

## Overview

This repository contains various Operating System concepts implemented in Java, focusing on memory management, page replacement algorithms, and synchronization using semaphores. These implementations help understand fundamental OS topics like paging, scheduling, and process synchronization.

## Contents

### 1. Page Replacement Algorithms (`pageReplacementAlgo`)

**Files:**

- `MemManage.java`
- `PageReplacementAlgo.java`
- `MemManageExp.java`

This section implements FIFO, CLOCK, LRU, and COUNT page replacement algorithms:

- **FIFO (First In, First Out)**: The oldest page in memory is replaced first.
- **CLOCK (Second-Chance Algorithm)**: Uses a "used" bit to determine if a page should be replaced.
- **LRU (Least Recently Used)**: Replaces the least recently used page.
- **COUNT (Least Frequently Used - LFU)**: Replaces the page with the lowest access count.

### 2. Semaphore-Based Ferry Simulation (`semaphores`)

**Files:**

- `FerrySimulation.java`

This section simulates a ferry between two ports using Java semaphores for synchronization.

- **Vehicles wait at the port** until the ferry arrives.
- **Ferry can carry up to 5 vehicles or 4 vehicles + 1 ambulance**.
- **Ambulance gets priority** (the ferry departs immediately when an ambulance boards).
- **Synchronization ensures proper boarding, crossing, and disembarking.**

## Installation & Execution

### Prerequisites

- **Java 8 or later** installed
- **Git** installed for cloning the repository

### Clone the Repository

```sh
git clone https://github.com/baris-aydin/operating-system-concepts.git
```

### Compile and Run

#### Run Page Replacement Algorithms

```sh
javac MemManageExp.java
java MemManageExp
```

#### Run Ferry Simulation

```sh
javac FerrySimulation.java
java FerrySimulation
```

## Sample Output

### Page Replacement Algorithm Output

```sh
Running simulation using FIFO
Number of faults per 1000 references: 57

Running simulation using CLOCK
Number of faults per 1000 references: 52

Running simulation using LRU
Number of faults per 1000 references: 47
```

### Ferry Simulation Output

```sh
Ferry starts at port 0
Auto 1 arrives at port 0
Auto 1 boards the ferry at port 0
Ambulance arrives at port 0
Ambulance boards the ferry at port 0
Ferry departs from port 0
Ferry arrives at port 1
Auto 1 disembarks at port 1
Ambulance disembarks at port 1
```



