INSERT INTO categories (name, description) VALUES
  ('Cleaning Supplies', 'Products for cleaning and maintaining household hygiene'),
  ('Groceries', 'Everyday food and beverage items for your kitchen and pantry'),
  ('Personal Care', 'Hygiene and grooming products for personal use'),
  ('Pet Supplies', 'Food, toys, and accessories for your pets'),
  ('Office Supplies', 'Stationery, paper, and office essentials'),
  ('Laundry Supplies', 'Detergents, fabric softeners, and stain removers for laundry'),
  ('Kitchen Essentials', 'Utensils, cookware, and tools for cooking and dining'),
  ('Toiletries', 'Soap, shampoo, toothpaste, and other bathroom essentials'),
  ('Baby Care', 'Diapers, wipes, and baby food for infant care'),
  ('Health & Wellness', 'Vitamins, supplements, and first-aid supplies'),
  ('Storage & Organization', 'Bins, boxes, and shelves to organize your home'),
  ('Home Maintenance', 'Tools, hardware, and repair kits for home upkeep'),
  ('Garden Supplies', 'Seeds, fertilizers, and tools for gardening'),
  ('Beverages', 'Bottled drinks, tea, coffee, and juices'),
  ('Snacks & Treats', 'Chips, candies, and other snackable items')
ON CONFLICT (name)
DO NOTHING;