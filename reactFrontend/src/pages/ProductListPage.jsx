import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ProductTable from "../components/ProductTable";
import { fetchProductsPaged } from "../services/productService";

function ProductListPage() {
  const [products, setProducts] = useState([]);
  const [pageInfo, setPageInfo] = useState({
    number: 0,
    totalPages: 0,
    totalElements: 0,
    size: 5,
  });

  const navigate = useNavigate();

  const loadProducts = async (page = 0) => {
    try {
      const data = await fetchProductsPaged(page, 5, "id", "asc");
      setProducts(data.content || []);
      setPageInfo({
        number: data.number,
        totalPages: data.totalPages,
        totalElements: data.totalElements,
        size: data.size,
      });
    } catch (error) {
      console.error("Error fetching paginated products:", error);
    }
  };

  useEffect(() => {
    loadProducts(0);
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <h1>Products Page</h1>

      <div style={{ marginBottom: "15px" }}>
        <button onClick={() => navigate("/add-product")}>
          Add Product
        </button>
      </div>

      <ProductTable products={products} />

      <div style={{ marginTop: "20px" }}>
        <button
          onClick={() => loadProducts(pageInfo.number - 1)}
          disabled={pageInfo.number === 0}
        >
          Previous
        </button>

        <span style={{ margin: "0 10px" }}>
          Page {pageInfo.number + 1} of {pageInfo.totalPages}
        </span>

        <button
          onClick={() => loadProducts(pageInfo.number + 1)}
          disabled={pageInfo.number + 1 >= pageInfo.totalPages}
        >
          Next
        </button>
      </div>
    </div>
  );
}

export default ProductListPage;