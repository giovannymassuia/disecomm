---
name: InventoryUpdate
version: 0.0.1
summary: |
  Event raised when the inventory has been updated.
producers:
  - Inventory Service
consumers:
  - Order Management Service
owners:
  - giovannymassuia
---

<Admonition>When firing this event make sure you set the `correlation-id` in the headers. Our
schemas have standard metadata make sure you read and follow it.</Admonition>

### Details

This event is the final event of the ordering process. It gets raised when the shipment has been
delivered.

<NodeGraph title="Consumer / Producer Diagram" />

<Schema />
