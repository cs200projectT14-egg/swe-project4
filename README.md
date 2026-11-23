# SWE Project 4

This is the entire ChocAn system, implemented in Java.

## Building

Build the project with:
```sh
make
```

Clean the source tree with:
```sh
make clean
```

## Usage

Run the program with:
```sh
make run
```

You'll be prompted to login with an ID. All IDs are visible in the [people
database](./src/chocan.db). For instance, you might login as an operator with
ID `o123`.

### Operator Actions

Login as an operator with ID `o123`.

From there, you may add, edit, or delete members:
- Add a member: `am c9|John Brown|789 Elm Street|active`
- Edit the member's status: `em c9|John Brown|789 Elm Street|suspended`
- Delete the member: `dm c9`

You may add, edit, or delete providers similarly, though note they don't have a
status:
- Add a provider: `ap p11|Jack Black|545 Elm Street`
- Edit the provider's address: `ep p11|Jack Black|111 Provider Avenue`
- Delete the provider: `dp p11`

Changes are kept in memory until you logout and exit the program:
- Logout: `l`
- Exit the program: `exit` (from the main menu)

Upon graceful shutdown, changes are written to disk.

### Manager Actions

Login as a manager with ID `m1`.

From there, you may do accounting services with `da`, which will write some
reports to disk under `./src/reports/` and `./src/clients/`. That's all
managers may do.

Logout with `l`.

### Provider Actions

Login as a provider with ID `p123`.

The following actions are available to you:
- Open the provider directory with `op`, which dumps the contents to the
  console.
- Verify a member by their ID, e.g., with `vm c2`.
- Report a service with `rs`, whereupon the provider is prompted for service
  details.

Finally, logout with `l`.
