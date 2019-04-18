



-- 1. ¿Se puede producir las ordenes en estado confirmado? 

SELECT p.id, p.description, (tmp.qty - p.qty) AS qty
FROM products p
INNER JOIN (
    SELECT ap.product_id, SUM(oa.qty*ap.qty) as qty
    FROM orders o
    INNER JOIN order_assemblies oa ON (o.id = oa.id)
    INNER JOIN assembly_products ap ON (oa.assembly_id = ap.id)
    WHERE o.status_id = 2
    GROUP BY ap.product_id ) as tmp ON (p.id = tmp.product_id)
WHERE (p.qty - tmp.qty) < 0;

SELECT COUNT(id) FROM assembly_products;