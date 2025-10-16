-- Migration V5: Populate sample data for Facilities and Inventory domains
-- Inserts sample data into the facilities and inventory tables

-- =========================================================
-- FACILITIES DATA (Public Schema)
-- =========================================================

-- Insert sample logs
INSERT INTO logs (date) VALUES 
    (CURRENT_DATE - INTERVAL '5 days'),
    (CURRENT_DATE - INTERVAL '3 days'),
    (CURRENT_DATE - INTERVAL '1 day'),
    (CURRENT_DATE)
ON CONFLICT DO NOTHING;

-- Insert sample machines
INSERT INTO machines (name) VALUES 
    ('Industrial Conveyor Belt A'),
    ('Hydraulic Press Unit 1'),
    ('CNC Milling Machine 01'),
    ('Assembly Robot Arm 02'),
    ('Packaging Machine Unit B')
ON CONFLICT DO NOTHING;

-- Insert sample parts
INSERT INTO parts (name, machine_id) VALUES 
    ('Motor Bearing', (SELECT id FROM machines WHERE name = 'Industrial Conveyor Belt A' LIMIT 1)),
    ('Hydraulic Pump', (SELECT id FROM machines WHERE name = 'Hydraulic Press Unit 1' LIMIT 1)),
    ('Spindle Assembly', (SELECT id FROM machines WHERE name = 'CNC Milling Machine 01' LIMIT 1)),
    ('Control Board', (SELECT id FROM machines WHERE name = 'Assembly Robot Arm 02' LIMIT 1)),
    ('Drive Belt', (SELECT id FROM machines WHERE name = 'Packaging Machine Unit B' LIMIT 1)),
    ('Electrical Connector', NULL)
ON CONFLICT DO NOTHING;

-- Insert sample reports
INSERT INTO reports (report_text, log_id, part_id, machine_id, needs_repair) VALUES 
    ('Routine maintenance performed', (SELECT id FROM logs WHERE date = CURRENT_DATE - INTERVAL '5 days' LIMIT 1), NULL, (SELECT id FROM machines WHERE name = 'Industrial Conveyor Belt A' LIMIT 1), FALSE),
    ('Bearing replacement needed', (SELECT id FROM logs WHERE date = CURRENT_DATE - INTERVAL '3 days' LIMIT 1), (SELECT id FROM parts WHERE name = 'Motor Bearing' LIMIT 1), (SELECT id FROM machines WHERE name = 'Industrial Conveyor Belt A' LIMIT 1), TRUE),
    ('Pressure calibration check', (SELECT id FROM logs WHERE date = CURRENT_DATE - INTERVAL '1 day' LIMIT 1), NULL, (SELECT id FROM machines WHERE name = 'Hydraulic Press Unit 1' LIMIT 1), FALSE),
    ('Emergency shutdown activated', (SELECT id FROM logs WHERE date = CURRENT_DATE LIMIT 1), (SELECT id FROM parts WHERE name = 'Control Board' LIMIT 1), (SELECT id FROM machines WHERE name = 'Assembly Robot Arm 02' LIMIT 1), TRUE)
ON CONFLICT DO NOTHING;

-- =========================================================
-- INVENTORY DATA (Inventory Schema)
-- =========================================================

-- Insert sample inventory items
INSERT INTO inventory.items (sku, name, reorder_point) VALUES 
    ('SKU-001', 'Motor Oil 5L', 10),
    ('SKU-002', 'Industrial Bearings (Set of 10)', 5),
    ('SKU-003', 'Hydraulic Fluid 10L', 8),
    ('SKU-004', 'Electrical Connectors Box', 20),
    ('SKU-005', 'Drive Belts Assortment', 12),
    ('SKU-006', 'Control Board PCB', 3),
    ('SKU-007', 'Lubricating Grease 500g', 25),
    ('SKU-008', 'Replacement Spindle Assembly', 2)
ON CONFLICT (sku) DO NOTHING;

-- Insert initial stock levels for each item
INSERT INTO inventory.stock_levels (item_id, level) 
SELECT id, 
  CASE sku
    WHEN 'SKU-001' THEN 35
    WHEN 'SKU-002' THEN 8
    WHEN 'SKU-003' THEN 15
    WHEN 'SKU-004' THEN 45
    WHEN 'SKU-005' THEN 18
    WHEN 'SKU-006' THEN 4
    WHEN 'SKU-007' THEN 60
    WHEN 'SKU-008' THEN 1
    ELSE 0
  END as initial_level
FROM inventory.items
ON CONFLICT (item_id) DO NOTHING;

-- Insert stock ledger entries (transaction history)
INSERT INTO inventory.stock_ledger (item_id, delta, reason, ref_id) 
SELECT 
  id,
  CASE sku
    WHEN 'SKU-001' THEN 35
    WHEN 'SKU-002' THEN 8
    WHEN 'SKU-003' THEN 15
    WHEN 'SKU-004' THEN 45
    WHEN 'SKU-005' THEN 18
    WHEN 'SKU-006' THEN 4
    WHEN 'SKU-007' THEN 60
    WHEN 'SKU-008' THEN 1
    ELSE 0
  END,
  'Initial stock',
  'INIT-' || sku
FROM inventory.items
ON CONFLICT (ref_id) DO NOTHING;

-- Additional stock movements
INSERT INTO inventory.stock_ledger (item_id, delta, reason, ref_id) VALUES 
    ((SELECT id FROM inventory.items WHERE sku = 'SKU-001' LIMIT 1), -5, 'Machine maintenance usage', 'MNT-20251016-001'),
    ((SELECT id FROM inventory.items WHERE sku = 'SKU-002' LIMIT 1), -2, 'Equipment repair', 'REP-20251016-001'),
    ((SELECT id FROM inventory.items WHERE sku = 'SKU-003' LIMIT 1), -3, 'System refill', 'REF-20251016-001'),
    ((SELECT id FROM inventory.items WHERE sku = 'SKU-007' LIMIT 1), -8, 'Routine lubrication', 'LUB-20251016-001')
ON CONFLICT (ref_id) DO NOTHING;

-- Update stock levels after movements
UPDATE inventory.stock_levels 
SET level = (
  SELECT COALESCE(SUM(delta), 0) 
  FROM inventory.stock_ledger 
  WHERE item_id = inventory.stock_levels.item_id
)
WHERE item_id IN (SELECT id FROM inventory.items);
