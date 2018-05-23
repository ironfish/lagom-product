# LAGOM-PRODUCT

[![Build Status](https://travis-ci.com/ironfish/lagom-product.svg?branch=master)](https://travis-ci.com/ironfish/lagom-product)

Example Product that supports the following construct.

```java
public interface ProductEvent {

    String getId();
    String getName();
    String getCost();
    String getRating();
}
```

## Usage

### Get Example
```sbtshell
curl http://localhost:9000/api/products/product/1234
```

### Post Example
```sbtshell
curl -H "Content-Type: application/json" -X POST -d '{"name": "Socks", "cost": "13.00", "rating": "10"}' http://localhost:9000/api/products/1234
```
