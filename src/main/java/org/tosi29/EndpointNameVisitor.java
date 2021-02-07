package org.tosi29;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.Optional;

public class EndpointNameVisitor extends VoidVisitorAdapter<List<EndPoint>> {
    @Override
    public void visit(MethodDeclaration method, List<EndPoint> endPoints) {
        Optional<AnnotationExpr> requestMapping = method.getAnnotationByName("RequestMapping");
        Optional<AnnotationExpr> getMapping = method.getAnnotationByName("GetMapping");
        Optional<AnnotationExpr> postMapping = method.getAnnotationByName("PostMapping");

        if (requestMapping.isPresent()) {
            String path = parseEndpointPath(requestMapping.get());
            String httpMethod = parseEndpointMethod(requestMapping.get());
            endPoints.add(new EndPoint(path, httpMethod));
        }
        if (getMapping.isPresent()) {
            String path = parseEndpointPath(getMapping.get());
            endPoints.add(new EndPoint(path, "GET"));
        }
        if (postMapping.isPresent()) {
            String path = parseEndpointPath(postMapping.get());
            endPoints.add(new EndPoint(path, "POST"));
        }
    }

    private String parseEndpointPath(AnnotationExpr expr) {
        if (expr.isSingleMemberAnnotationExpr()) {
            return ((SingleMemberAnnotationExpr) expr).getMemberValue().toString();
        }
        if (expr.isNormalAnnotationExpr()) {
            NodeList<MemberValuePair> nodeList = ((NormalAnnotationExpr) expr).getPairs();

            // valueもしくはpathを取り出す。もし両方指定されている場合は、以下のようになるので許容する。
            // ・同一の値が指定されている場合：同一のため、どちらを取り出しても問題なし。Spring Bootとしても正常に起動する。
            // ・異なる値が指定されている場合：Spring Bootの起動時にエラーとなり正常動作しないため、実質的に問題にならない
            Optional<MemberValuePair> memberValuePair = nodeList.stream()
                    .filter(x -> x.getNameAsString().equals("value") || x.getNameAsString().equals("path"))
                    .findFirst();

            // memberValuePairは必ず存在するはずなので、そのままgetする
            return memberValuePair.get().getValue().toString();
        }
        if (expr.isMarkerAnnotationExpr()) {
            // This pattern should not exist
        }

        throw new RuntimeException("Unknown AnnotationExpr");
    }

    private String parseEndpointMethod(AnnotationExpr expr) {
        if (expr.isSingleMemberAnnotationExpr()) {
            return "";
        }
        if (expr.isNormalAnnotationExpr()) {
            NodeList<MemberValuePair> nodeList = ((NormalAnnotationExpr) expr).getPairs();

            Optional<MemberValuePair> memberValuePair = nodeList.stream()
                    .filter(x -> x.getNameAsString().equals("method"))
                    .findFirst();

            if (memberValuePair.isPresent()) {
                return memberValuePair.get().toString();
            } else {
                return "";
            }
        }
        throw new RuntimeException("Unknown AnnotationExpr");
    }


}
